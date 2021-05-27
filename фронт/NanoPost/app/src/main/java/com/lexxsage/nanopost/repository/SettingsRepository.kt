package com.lexxsage.nanopost.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    var authToken: String?
        get() = runBlocking { dataStore.data.first()[authTokenKey] }
        set(value) = runBlocking {
            dataStore.edit { settings ->
                if (value != null) {
                    settings[authTokenKey] = value
                } else {
                    settings.remove(authTokenKey)
                }
            }
        }

    fun authToken() = dataStore.data
        .map { it[authTokenKey] }
        .distinctUntilChanged()

    fun authorized() = dataStore.data
        .map { it[authTokenKey] != null }
        .distinctUntilChanged()

    companion object {
        val authTokenKey = stringPreferencesKey("authToken")
    }
}
