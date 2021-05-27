package com.lexxsage.nanopost.network

import com.lexxsage.nanopost.network.model.LoginResponse
import com.lexxsage.nanopost.network.model.RegisterRequest
import com.lexxsage.nanopost.network.model.ResultResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApiService {

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): ResultResponse

    @GET("login")
    suspend fun login(
        @Query("username") username: String,
        @Query("password") password: String,
    ): LoginResponse

    @GET("utils/checkUsername")
    suspend fun checkUsername(username: String): ResultResponse
}