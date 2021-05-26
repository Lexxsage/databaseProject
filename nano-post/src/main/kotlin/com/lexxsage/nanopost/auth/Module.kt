package com.lexxsage.nanopost.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.lexxsage.nanopost.BackendException
import com.lexxsage.nanopost.db.DAO
import com.lexxsage.nanopost.db.UserEntity
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import java.util.*

fun Application.installAuth() = Auth(this).also(Auth::install)

class Auth(private val application: Application) {
    private val issuer = application.environment.config.property("jwt.domain").getString()
    private val secret = application.environment.config.property("jwt.secret").getString()
    private val algorithm = Algorithm.HMAC512(secret)

    private val verifier = JWT.require(algorithm)
        .withSubject("Authentication")
        .withIssuer(issuer)
        .build()

    fun install() {
        application.install(Authentication) {
            jwt {
                verifier(verifier)
                validate { cred ->
                    try {
                        val id = cred.payload.getClaim("id").asString()
                        val user = DAO.getUser(UUID.fromString(id))
                        UserPrincipal(user).takeIf {
                            cred.payload.issuedAt.time == user.passwordUpdated
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null
                    }
                }
            }
        }
    }

    suspend fun createUser(request: AuthRequest) = DAO.createUser(
        username = request.username,
        passwordHash = hashPassword(request.password),
    )

    fun generateToken(id: UUID, passwordUpdated: Long): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withClaim("id", id.toString())
        .withIssuedAt(Date(passwordUpdated))
        .sign(algorithm)
}

@Serializable
data class AuthRequest(
    val username: String,
    val password: String,
)

@Serializable
data class AuthResponse(
    val userId: String,
    val token: String,
    val expiresIn: Long = 0,
)

class InvalidPasswordException : BackendException(HttpStatusCode.BadRequest, "INVALID_PASSWORD")
class UserNotFoundException : BackendException(HttpStatusCode.BadRequest, "USER_NOT_FOUND")

@JvmInline
value class UserPrincipal(val user: UserEntity) : Principal

val ApplicationCall.user get() = principal<UserPrincipal>()!!.user
