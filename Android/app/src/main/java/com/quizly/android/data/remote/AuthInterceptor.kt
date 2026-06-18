package com.quizly.android.data.remote

import com.quizly.android.data.local.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

/** Mirrors api_client.py's `_auth_headers()` - injects X-Auth-Token on every request when present. */
class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sessionManager.token()
        val request = if (token != null) {
            chain.request().newBuilder().addHeader("X-Auth-Token", token).build()
        } else {
            chain.request()
        }

        val response = chain.proceed(request)

        // No refresh-token flow exists upstream (same as the WPF/Python clients) - on a
        // 401/403 the only option is to bounce the user back to Login.
        if ((response.code == 401 || response.code == 403) && token != null) {
            sessionManager.notifyForceLogout()
        }

        return response
    }
}
