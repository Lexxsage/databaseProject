package com.lexxsage.nanopost.ui.feed

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.lexxsage.nanopost.network.NanoPostApiService
import com.lexxsage.nanopost.paging.StringKeyedPagingSource
import com.lexxsage.nanopost.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val apiService: NanoPostApiService,
) : BaseViewModel() {

    val pager = Pager(PagingConfig(30, enablePlaceholders = false)) {
        StringKeyedPagingSource { count, nextFrom ->
            apiService.getFeed(count, nextFrom)
        }
    }.flow
        .cachedIn(viewModelScope)
}
