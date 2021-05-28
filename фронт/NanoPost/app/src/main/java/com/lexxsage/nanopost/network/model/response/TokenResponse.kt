package com.lexxsage.nanopost.network.model.response

import com.lexxsage.nanopost.network.model.UserApiModel
import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val user: UserApiModel,
    val token: String,
    val expiresIn: Long? = 0L,
)
