package com.lexxsage.nanopost.ui.profile

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.lexxsage.nanopost.network.NanoPostApiService
import com.lexxsage.nanopost.network.model.UserApiModel
import com.lexxsage.nanopost.paging.StringKeyedPagingSource
import com.lexxsage.nanopost.repository.AuthRepository
import com.lexxsage.nanopost.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val apiService: NanoPostApiService,
    private val authRepository: AuthRepository,
) : BaseViewModel() {

    val pager = Pager(PagingConfig(30, enablePlaceholders = false)) {
        StringKeyedPagingSource { count, nextFrom ->
            apiService.getUserPosts(authRepository.userId, count, nextFrom)
        }
    }.flow
        .cachedIn(viewModelScope)

    suspend fun getProfile(id: String?) = flow {
        emit(apiService.getUser(id))
    }.stateIn(viewModelScope)
}
