package com.lexxsage.nanopost.routes

import com.lexxsage.nanopost.Likes
import com.lexxsage.nanopost.auth.user
import com.lexxsage.nanopost.db.DAO
import com.lexxsage.nanopost.likes
import com.lexxsage.nanopost.toDomainModel
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class PostCreateRequest(
    val text: String? = null,
    val imageId: String? = null,
)

fun Route.createPost() = post<PostCreateRequest>("/post/create") { body ->
    val post = DAO.createPost(call.user, body.text, body.imageId)
    call.respond(post.toDomainModel(Likes.Default))
}

fun Route.getPost() = get("/post/{id}") {
    val post = DAO.getPost(call.parameters["id"]!!)
    call.respond(post.toDomainModel(post.likes().toDomainModel(call.user.id.toString())))
}

fun Route.likePost() = post("/post/{id}/like") {
    val postId = call.parameters["id"]!!
    val post = DAO.getPost(postId)
    val likes = DAO.likePost(postId, call.user)
    call.respond(post.toDomainModel(Likes(
        liked = true,
        likesCount = likes.count().toInt(),
    )))
}

fun Route.unlikePost() = post("/post/{id}/unlike") {
    val postId = call.parameters["id"]!!
    val post = DAO.getPost(postId)
    val likes = DAO.unlikePost(postId, call.user)
    call.respond(post.toDomainModel(Likes(
        liked = false,
        likesCount = likes.count().toInt(),
    )))
}
