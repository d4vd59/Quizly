package com.quizly.android.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** SPECULATIVE: not in API docs, verify against live server. Inferred shape for /invite. */
@Serializable
data class Invite(
    @SerialName("invite_id")
    val inviteId: Int,
    val from: String? = null,
    val friend: String? = null,
    val message: String? = null,
    val status: String? = null
)
