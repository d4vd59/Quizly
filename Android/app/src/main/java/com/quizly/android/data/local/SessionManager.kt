package com.quizly.android.data.local

import com.quizly.android.data.model.AuthToken
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * In-memory mirror of the persisted [TokenStore], read synchronously by [AuthInterceptor]
 * without needing runBlocking on OkHttp's dispatcher thread. Equivalent of api_client.py
 * keeping self.auth_token as an instance field after _load_token().
 */
class SessionManager {

    private val _currentUser = MutableStateFlow<AuthToken?>(null)
    val currentUser: StateFlow<AuthToken?> = _currentUser.asStateFlow()

    private val _forceLogoutEvents = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val forceLogoutEvents: SharedFlow<Unit> = _forceLogoutEvents.asSharedFlow()

    fun token(): String? = _currentUser.value?.token

    fun set(authToken: AuthToken) {
        _currentUser.value = authToken
    }

    fun clear() {
        _currentUser.value = null
    }

    fun notifyForceLogout() {
        _currentUser.value = null
        _forceLogoutEvents.tryEmit(Unit)
    }
}
