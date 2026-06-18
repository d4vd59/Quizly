package com.quizly.android.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignInRequest(val password: String)

@Serializable
data class SignInResponse(
    @SerialName("X-Auth-Token")
    val authToken: String? = null,
    @SerialName("Valid_until")
    val validUntil: Long? = null
)

@Serializable
data class SignUpRequest(
    val nickname: String,
    val fullname: String,
    val password: String
)

@Serializable
data class SignUpResponse(
    @SerialName("Message")
    val message: String? = null,
    @SerialName("EmailValidationToken")
    val emailValidationToken: String? = null
)

@Serializable
data class GenericMessageResponse(
    @SerialName("Message")
    val message: String? = null
)

@Serializable
data class UpdateUserRequest(
    @SerialName("current_password")
    val currentPassword: String,
    val nickname: String? = null,
    val fullname: String? = null,
    val email: String? = null,
    @SerialName("new_password")
    val newPassword: String? = null
)
