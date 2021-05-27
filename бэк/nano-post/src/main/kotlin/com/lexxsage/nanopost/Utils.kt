package com.lexxsage.nanopost

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.serialization.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder
import java.util.*

open class BackendException(
    val status: HttpStatusCode,
    val code: String,
) : Exception(code)

@Serializable
data class ErrorResponse(
    val code: String = "UNKNOWN_ERROR",
    val message: String? = null,
)

inline fun ContentNegotiation.Configuration.json(crossinline builder: JsonBuilder.() -> Unit) {
    json(Json { builder() })
}

val ApplicationCall.query get() = request.queryParameters

fun String.uuid(): UUID = UUID.fromString(this)
