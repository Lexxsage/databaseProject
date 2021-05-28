package com.lexxsage.nanopost.ui.auth.login

import androidx.lifecycle.viewModelScope
import com.lexxsage.nanopost.repository.AuthRepository
import com.lexxsage.nanopost.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : BaseViewModel() {

    val result by authRepository::authorized

    fun login(
        username: String,
        password: String,
    ) = viewModelScope.launchCatching {
        authRepository.login(username, password)
    }
}
