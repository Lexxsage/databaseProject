package com.lexxsage.nanopost.routes

import com.lexxsage.nanopost.auth.*
import com.lexxsage.nanopost.db.DAO
import com.lexxsage.nanopost.query
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.login(auth: Auth) = get("/login") {
    val body = AuthRequest(
        username = call.query["username"]!!,
        password = call.query["password"]!!,
    )
    val user = DAO.findUser(body.username) ?: throw UserNotFoundException()
    if (verifyPassword(body.password, user.passwordHash)) {
        call.respond(
            HttpStatusCode.OK,
            AuthResponse(
                userId = user.id.value.toString(),
                token = auth.generateToken(user.id.value, user.passwordUpdated),
            ),
        )
    } else {
        throw InvalidPasswordException()
    }
}

fun Route.register(auth: Auth) = post<AuthRequest>("/register") { body ->
    auth.createUser(body)
    call.respond(HttpStatusCode.OK, ResultResponse())
}
