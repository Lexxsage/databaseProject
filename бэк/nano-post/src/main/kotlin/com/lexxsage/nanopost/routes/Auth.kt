package com.lexxsage.nanopost.routes

import com.lexxsage.nanopost.auth.*
import com.lexxsage.nanopost.avatar
import com.lexxsage.nanopost.db.DAO
import com.lexxsage.nanopost.query
import com.lexxsage.nanopost.toDomainModel
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.login(auth: Auth) = get("/login") {
    val body = AuthRequest(
        username = call.query["username"]!!,
        password = call.query["password"]!!,
    )
    val user = DAO.findUser(body.username) ?: throw UserNotFoundException()
    if (verifyPassword(body.password, user.passwordHash)) {
        val avatar = user.avatar()?.toDomainModel()
        call.respond(auth.createTokenResponse(
            user.toDomainModel(avatar),
            user.passwordUpdated,
        ))
    } else {
        throw InvalidPasswordException()
    }
}

fun Route.register(auth: Auth) = post<AuthRequest>("/register") { body ->
    val user = auth.createUser(body)
    val avatar = user.avatar()?.toDomainModel()
    call.respond(auth.createTokenResponse(
        user.toDomainModel(avatar),
        user.passwordUpdated,
    ))
}
