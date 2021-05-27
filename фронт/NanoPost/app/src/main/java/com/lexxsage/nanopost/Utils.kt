@file:OptIn(ExperimentalCoroutinesApi::class)
package com.lexxsage.nanopost

import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

suspend inline fun <T> collectErrors(buffer: MutableSharedFlow<Exception>?, body: () -> T) = try {
    body()
} catch (e: CancellationException) {
    throw e
} catch (e: Exception) {
    buffer?.emit(e)
    null
}

fun EditText.textChanges() = callbackFlow {
    val watcher = doAfterTextChanged { it?.let(::offer) }

    awaitClose {
        removeTextChangedListener(watcher)
    }
}.distinctUntilChanged()

fun EditText.focus() = callbackFlow {
    setOnFocusChangeListener { _, hasFocus ->
        offer(hasFocus)
    }

    awaitClose {
        onFocusChangeListener = null
    }
}
