package com.lexxsage.nanopost.network

import com.lexxsage.nanopost.network.model.response.TokenResponse
import com.lexxsage.nanopost.network.model.request.RegisterRequest
import com.lexxsage.nanopost.network.model.response.ResultResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApiService {

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): TokenResponse

    @GET("login")
    suspend fun login(
        @Query("username") username: String,
        @Query("password") password: String,
    ): TokenResponse

    @GET("utils/checkUsername")
    suspend fun checkUsername(username: String): ResultResponse
}