package com.lexxsage.nanopost.network.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val userId: String,
    val token: String,
    val expiresIn: Long,
)
