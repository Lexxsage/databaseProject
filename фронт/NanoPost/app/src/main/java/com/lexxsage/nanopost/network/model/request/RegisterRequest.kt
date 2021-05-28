package com.lexxsage.nanopost.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val username: String,
    val password: String,
)
