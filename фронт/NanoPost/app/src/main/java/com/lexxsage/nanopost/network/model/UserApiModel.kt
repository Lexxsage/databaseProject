package com.lexxsage.nanopost.network.model

import kotlinx.serialization.Serializable

@Serializable
data class UserApiModel(
    val id: String,
    val username: String,
    val displayName: String? = null,
    val bio: String? = null,
    val avatar: ImageApiModel? = null,
    val subscribed: Boolean? = null,
)
