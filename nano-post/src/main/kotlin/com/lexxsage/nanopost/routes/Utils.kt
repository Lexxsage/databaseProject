package com.lexxsage.nanopost.routes

import com.lexxsage.nanopost.db.DAO
import com.lexxsage.nanopost.query
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.checkUsername() = get("/utils/checkUsername") {
    val username = call.query["username"]
    call.respond(ResultResponse(
        !username.isNullOrEmpty() && DAO.findUser(username) == null
    ))
}
