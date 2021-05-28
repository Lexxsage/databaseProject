package com.lexxsage.nanopost.network

import com.lexxsage.nanopost.network.model.PostApiModel
import com.lexxsage.nanopost.network.model.UserApiModel
import com.lexxsage.nanopost.network.model.response.PagedDataResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NanoPostApiService {

    @GET("feed")
    suspend fun getFeed(
        @Query("count") count: Int,
        @Query("nextFrom") nextFrom: String?,
    ): PagedDataResponse<PostApiModel>

    @GET("user/{id}")
    suspend fun getUser(
        @Path("id") id: String?
    ): UserApiModel

    @GET("user/{id}/posts")
    suspend fun getUserPosts(
        @Path("id") id: String?,
        @Query("count") count: Int,
        @Query("nextFrom") nextFrom: String?,
    ): PagedDataResponse<PostApiModel>
}
