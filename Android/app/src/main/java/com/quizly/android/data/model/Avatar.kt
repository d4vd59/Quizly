package com.quizly.android.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Ported from avatar.py (no C# equivalent) - response shape of GET /user/{user}/avatar?type=json. */
@Serializable
data class Avatar(
    @SerialName("mime_type")
    val mimeType: String,
    val avatar: String
)
