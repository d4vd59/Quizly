package com.quizly.android.data.repository

import com.quizly.android.data.local.SessionManager
import com.quizly.android.data.local.TokenStore
import com.quizly.android.data.model.AuthToken
import com.quizly.android.data.remote.ApiResult
import com.quizly.android.data.remote.ApiService
import com.quizly.android.data.remote.dto.GenericMessageResponse
import com.quizly.android.data.remote.dto.SignInRequest
import com.quizly.android.data.remote.dto.SignInResponse
import com.quizly.android.data.remote.dto.SignUpRequest
import com.quizly.android.data.remote.dto.SignUpResponse
import com.quizly.android.data.remote.dto.UpdateUserRequest

/** Ported from api_client.py signup/signin/signout/verify_email/update_user/request_password_reset. */
class AuthRepository(
    private val api: ApiService,
    private val tokenStore: TokenStore,
    private val sessionManager: SessionManager
) {

    suspend fun signup(email: String, nickname: String, fullname: String, password: String): ApiResult<SignUpResponse> =
        safeCall { api.signup(email, SignUpRequest(nickname, fullname, password)) }

    suspend fun signin(user: String, password: String): ApiResult<SignInResponse> {
        val result = safeCall { api.signin(user, SignInRequest(password)) }
        if (result is ApiResult.Success && result.data.authToken != null) {
            val authToken = AuthToken(
                token = result.data.authToken,
                validUntil = result.data.validUntil,
                userIdentifier = user
            )
            tokenStore.save(authToken)
            sessionManager.set(authToken)
        }
        return result
    }

    suspend fun signout(user: String): ApiResult<GenericMessageResponse> {
        val result = safeCall { api.signout(user) }
        tokenStore.clear()
        sessionManager.clear()
        return result
    }

    suspend fun verifyEmail(email: String, token: String): ApiResult<GenericMessageResponse> =
        safeCall { api.verifyEmail(email, token) }

    suspend fun updateUser(user: String, request: UpdateUserRequest) =
        safeCall { api.updateUser(user, request) }

    suspend fun requestPasswordReset(email: String): ApiResult<GenericMessageResponse> =
        safeCall { api.requestPasswordReset(email) }

    /** Restores the in-memory session from the persisted token at app startup. */
    suspend fun restoreSession() {
        tokenStore.current()?.let { sessionManager.set(it) }
    }
}
