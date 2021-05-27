package com.lexxsage.nanopost.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

abstract class BaseViewModel : ViewModel() {

    protected val errors = MutableSharedFlow<Exception>(onBufferOverflow = BufferOverflow.DROP_OLDEST)

    fun errors(): SharedFlow<Exception> = errors

    fun CoroutineScope.launchCatching(
        body: suspend () -> Unit,
    ) = launch {
        try {
            body()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            errors.emit(e)
        }
    }
}
