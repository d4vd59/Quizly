package com.quizly.android.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.quizly.android.data.model.AuthToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.tokenDataStore by preferencesDataStore(name = "auth_token")

/** Mirrors api_client.py's TOKEN_FILE / _load_token / _save_token, backed by DataStore. */
class TokenStore(private val context: Context) {

    private object Keys {
        val TOKEN = stringPreferencesKey("token")
        val VALID_UNTIL = longPreferencesKey("valid_until")
        val USER_IDENTIFIER = stringPreferencesKey("user_identifier")
    }

    val tokenFlow: Flow<AuthToken?> = context.tokenDataStore.data.map { prefs ->
        val token = prefs[Keys.TOKEN] ?: return@map null
        val user = prefs[Keys.USER_IDENTIFIER] ?: return@map null
        AuthToken(token = token, validUntil = prefs[Keys.VALID_UNTIL], userIdentifier = user)
    }

    suspend fun current(): AuthToken? = tokenFlow.first()

    suspend fun save(token: AuthToken) {
        context.tokenDataStore.edit { prefs ->
            prefs[Keys.TOKEN] = token.token
            token.validUntil?.let { prefs[Keys.VALID_UNTIL] = it }
            prefs[Keys.USER_IDENTIFIER] = token.userIdentifier
        }
    }

    suspend fun clear() {
        context.tokenDataStore.edit { it.clear() }
    }
}
