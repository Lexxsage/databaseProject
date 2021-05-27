package com.lexxsage.nanopost.routes

import com.lexxsage.nanopost.*
import com.lexxsage.nanopost.auth.user
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun Route.getFeed() = get("/feed") {
    val count = call.query["count"]?.toIntOrNull() ?: 50
    val (startTime, offset) = call.query.decodeNextFrom()
    val posts = withContext(Dispatchers.Default) {
        call.user.subscriptions().flatMap { it.posts() }
    }
    call.respond(posts.pagedResponse(startTime, offset, count) { post ->
        post.toDomainModel(post.likes().toDomainModel(call.user.id.toString()))
    })
}
