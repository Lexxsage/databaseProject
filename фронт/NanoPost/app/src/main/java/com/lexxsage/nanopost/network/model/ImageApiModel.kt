package com.lexxsage.nanopost.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ImageApiModel(
    val id: String,
    val owner: UserApiModel,
    val dateCreated: Long,
    val width: Int,
    val height: Int,
    val url: String,
)
