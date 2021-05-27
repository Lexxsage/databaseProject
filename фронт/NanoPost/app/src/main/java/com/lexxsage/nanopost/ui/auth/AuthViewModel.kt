package com.lexxsage.nanopost.ui.auth

import androidx.lifecycle.viewModelScope
import com.lexxsage.nanopost.collectErrors
import com.lexxsage.nanopost.network.AuthApiService
import com.lexxsage.nanopost.network.model.RegisterRequest
import com.lexxsage.nanopost.repository.SettingsRepository
import com.lexxsage.nanopost.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val apiService: AuthApiService,
    private val settingsRepository: SettingsRepository,
) : BaseViewModel() {

    val result = settingsRepository.authorized()

    fun checkUsername(username: String) = viewModelScope.launchCatching {
        apiService.checkUsername(username).result
    }

    fun login(username: String, password: String) = viewModelScope.launchCatching {
        loginInternal(username, password)
    }

    fun register(
        username: String,
        password: String,
    ) = viewModelScope.launchCatching {
        apiService.register(RegisterRequest(username, password))
        loginInternal(username, password)
    }

    private suspend fun loginInternal(username: String, password: String) {
        val response = apiService.login(username, password)
        settingsRepository.authToken = response.token
    }
}
