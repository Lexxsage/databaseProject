package com.lexxsage.nanopost.network.model

import kotlinx.serialization.Serializable

@Serializable
data class PostApiModel(
    val id: String,
    val owner: UserApiModel,
    val dateCreated: Long,
    val text: String? = null,
    val image: ImageApiModel? = null,
    val likes: LikesApiModel,
)
