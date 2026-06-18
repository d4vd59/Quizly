package com.quizly.android.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** SPECULATIVE: not in API docs, verify against live server. Inferred shape for /leaderboard entries. */
@Serializable
data class LeaderboardEntry(
    val rank: Int? = null,
    @SerialName("user_id")
    val userId: Int? = null,
    val nickname: String? = null,
    val score: Int? = null
)
