package com.quizly.android.data.model

/** Mirrors api_client.py's .auth_token cache file ({"token":..., "expiry":...}). */
data class AuthToken(
    val token: String,
    val validUntil: Long?,
    val userIdentifier: String
)
