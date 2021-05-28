package com.lexxsage.nanopost.di

import com.lexxsage.nanopost.network.AuthApiService
import com.lexxsage.nanopost.network.AuthInterceptor
import com.lexxsage.nanopost.network.NanoPostApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideRetrofit(converterFactory: Converter.Factory): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.5:8080/")
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService = retrofit.create()

    @Provides
    fun provideApiService(
        retrofit: Retrofit,
        authInterceptor: AuthInterceptor,
    ): NanoPostApiService {
        return retrofit.newBuilder()
            .baseUrl("http://192.168.1.5:8080/")
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(authInterceptor)
                    .build()
            )
            .build()
            .create()
    }
}
