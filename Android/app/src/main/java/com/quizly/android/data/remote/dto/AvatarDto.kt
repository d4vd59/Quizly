package com.quizly.android.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UploadAvatarRequest(
    @kotlinx.serialization.SerialName("mime_type")
    val mimeType: String,
    val avatar: String
)
