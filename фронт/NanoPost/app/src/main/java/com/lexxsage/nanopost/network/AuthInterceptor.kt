package com.lexxsage.nanopost.network

import com.lexxsage.nanopost.repository.SettingsRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer ${settingsRepository.authToken}")
            .build()

        return chain.proceed(request)
    }
}
