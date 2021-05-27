package com.lexxsage.nanopost.routes

import com.google.firebase.cloud.StorageClient
import com.lexxsage.nanopost.auth.user
import com.lexxsage.nanopost.db.DAO
import com.lexxsage.nanopost.toDomainModel
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.Serializable
import java.util.*

private val storageBucket by lazy {
    StorageClient.getInstance().bucket()
}

@Serializable
data class ImageUploadRequest(
    val format: String = "jpg",
    val data: String,
    val width: Int,
    val height: Int,
)

fun Route.uploadImage() = post<ImageUploadRequest>("/image/upload") { body ->
    val id = UUID.randomUUID().toString()
    val blob = storageBucket.create("$id.${body.format}", Base64.getDecoder().decode(body.data))
    val image = DAO.createImage(
        id = id,
        owner = call.user,
        width = body.width,
        height = body.height,
        url = blob.mediaLink,
    )
    call.respond(image.toDomainModel())
}

fun Route.getImage() = get("/image/{id}") {
    val image = DAO.getImage(call.parameters["id"]!!)
    call.respond(image.toDomainModel())
}
