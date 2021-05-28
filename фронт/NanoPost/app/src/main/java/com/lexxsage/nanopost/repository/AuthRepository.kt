package com.lexxsage.nanopost.repository

import com.lexxsage.nanopost.network.AuthApiService
import com.lexxsage.nanopost.network.model.request.RegisterRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: AuthApiService,
    private val settingsRepository: SettingsRepository,
) {
    val userId by settingsRepository::userId
    val authorized: Flow<Boolean> get() = settingsRepository.authorized()

    suspend fun checkUsername(username: String) = apiService.checkUsername(username).result

    suspend fun register(username: String, password: String) {
        val response = apiService.register(RegisterRequest(username, password))
        settingsRepository.userId = response.user.id
        settingsRepository.authToken = response.token
    }

    suspend fun login(username: String, password: String) {
        val response = apiService.login(username, password)
        settingsRepository.userId = response.user.id
        settingsRepository.authToken = response.token
    }
}
