package com.lexxsage.nanopost.di

import com.lexxsage.nanopost.network.converter.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConverterModule {

    @Provides
    @Singleton
    fun provideJsonConverter() = Json {
        ignoreUnknownKeys = true
    }.asConverterFactory(MediaType.get("application/json"))
}
