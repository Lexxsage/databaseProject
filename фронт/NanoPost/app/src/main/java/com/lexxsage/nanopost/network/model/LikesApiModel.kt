package com.lexxsage.nanopost.network.model

import kotlinx.serialization.Serializable

@Serializable
data class LikesApiModel(
    val liked: Boolean,
    val likesCount: Int,
)
