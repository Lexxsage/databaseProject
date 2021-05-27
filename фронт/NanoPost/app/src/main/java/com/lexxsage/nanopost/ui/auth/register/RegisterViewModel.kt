package com.lexxsage.nanopost.ui.auth.register

import androidx.lifecycle.viewModelScope
import com.lexxsage.nanopost.collectErrors
import com.lexxsage.nanopost.repository.AuthRepository
import com.lexxsage.nanopost.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : BaseViewModel() {

    val result by authRepository::authorized

    suspend fun checkUsername(username: String) = collectErrors(null) {
        authRepository.checkUsername(username)
    }

    fun register(
        username: String,
        password: String,
    ) = viewModelScope.launchCatching {
        authRepository.register(username, password)
    }
}
