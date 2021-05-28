package com.lexxsage.nanopost.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class PagedDataResponse<T>(
    val count: Int,
    val total: Int,
    val nextFrom: String,
    val items: List<T>,
)
