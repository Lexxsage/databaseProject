package com.lexxsage.nanopost.routes

import com.lexxsage.nanopost.auth.user
import com.lexxsage.nanopost.db.DAO
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import java.util.*

fun Route.subscribeToUser() = post("/user/{id}/subscribe") {
    val id = UUID.fromString(call.parameters["id"]!!)
    require(id != call.user.id.value) { "You can't subscribe to yourself" }
    DAO.subscribe(call.user, id)
    DAO.getUser(id).pushToken?.let {
        //TODO: send push notification
    }
    call.respond(ResultResponse())
}

fun Route.unsubscribeFromUser() = post("/user/{id}/unsubscribe") {
    val id = UUID.fromString(call.parameters["id"]!!)
    require(id != call.user.id.value) { "You can't unsubscribe from yourself" }
    DAO.unsubscribe(call.user, id)
    call.respond(ResultResponse())
}
