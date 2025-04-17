package com.nrin31266.ecommercemultivendor.common

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

class AuthPreferences @Inject constructor(@ApplicationContext private val context: Context) {
    companion object {
        val JWT_KEY = stringPreferencesKey("jwt")
        val ROLE_KEY = stringPreferencesKey("role")
    }

    val jwtFlow: Flow<String?> = context.dataStore.data.map { it[JWT_KEY] }
    val roleFlow: Flow<String?> = context.dataStore.data.map { it[ROLE_KEY] }

    suspend fun saveAuth(jwt: String, role: String) {
        context.dataStore.edit {
            it[JWT_KEY] = jwt
            it[ROLE_KEY] = role
        }
    }

    suspend fun clearAuth() {
        context.dataStore.edit {
            it.remove(JWT_KEY)
            it.remove(ROLE_KEY)
        }
    }
}
