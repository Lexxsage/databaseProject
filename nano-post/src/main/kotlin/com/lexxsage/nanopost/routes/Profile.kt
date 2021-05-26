package com.lexxsage.nanopost.routes

import com.lexxsage.nanopost.*
import com.lexxsage.nanopost.auth.hashPassword
import com.lexxsage.nanopost.auth.user
import com.lexxsage.nanopost.db.DAO
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.Serializable
import java.util.*

fun Route.getUser() = get("/user/{id?}") {
    val user = call.parameters["id"]?.let { DAO.getUser(UUID.fromString(it)) }
        ?: call.user
    val avatar = user.avatar()?.toDomainModel()
    val subscribed = call.user.subscribedTo(user.id.value)
    call.respond(user.toDomainModel(avatar, subscribed))
}

fun Route.getUserImages() = get("/user/{id?}/images") {
    val user = call.parameters["id"]?.let { DAO.getUser(UUID.fromString(it)) }
        ?: call.user
    val count = call.query["count"]?.toIntOrNull() ?: 30
    val (startTime, offset) = call.query.decodeNextFrom()
    call.respond(user.images().pagedResponse(startTime, offset, count) { it.toDomainModel() })
}

fun Route.getUserPosts() = get("/user/{id?}/posts") {
    val user = call.parameters["id"]?.let { DAO.getUser(UUID.fromString(it)) }
        ?: call.user
    val count = call.query["count"]?.toIntOrNull() ?: 30
    val (startTime, offset) = call.query.decodeNextFrom()
    call.respond(user.posts().pagedResponse(startTime, offset, count) { post ->
        val likes = post.likes()
        post.toDomainModel(Likes(
            liked = likes.any { it.id.value == call.user.id.value },
            likesCount = likes.count().toInt(),
        ))
    })
}

fun Route.getUserSubscribers() = get("/user/{id?}/subscribers") {
    val user = call.parameters["id"]?.let { DAO.getUser(UUID.fromString(it)) }
        ?: call.user
    val subscribers = user.subscribers().map {
        val avatar = it.avatar()?.toDomainModel()
        it.toDomainModel(avatar)
    }
    call.respond(subscribers)
}

fun Route.getUserSubscribedTo() = get("/user/{id?}/subscribedTo") {
    val user = call.parameters["id"]?.let { DAO.getUser(UUID.fromString(it)) }
        ?: call.user
    val subscribedTo = user.subscriptions().map {
        val avatar = it.avatar()?.toDomainModel()
        it.toDomainModel(avatar)
    }
    call.respond(subscribedTo)
}

@Serializable
data class ProfileUpdateRequest(
    val username: String? = null,
    val displayName: String? = null,
    val bio: String? = null,
    val avatarId: String? = null,
)

fun Route.updateProfile() = post<ProfileUpdateRequest>("/profile/update") { body ->
    DAO.updateUser(call.user, body.username, body.displayName, body.bio, body.avatarId)
    call.respond(ResultResponse())
}

@Serializable
data class PasswordUpdateRequest(val password: String)

fun Route.updatePassword() = post<PasswordUpdateRequest>("/profile/updatePassword") { body ->
    DAO.updateUserPassword(call.user, hashPassword(body.password))
    call.respond(ResultResponse())
}

@Serializable
data class PushTokenRequest(val pushToken: String)

fun Route.registerPushReceiver() = post<PushTokenRequest>("/profile/pushRegister") { body ->
    DAO.registerPushToken(call.user, body.pushToken)
    call.respond(ResultResponse())
}

fun Route.unregisterPushReceiver() = post("/profile/pushUnregister") {
    DAO.unregisterPushToken(call.user)
    call.respond(ResultResponse())
}
