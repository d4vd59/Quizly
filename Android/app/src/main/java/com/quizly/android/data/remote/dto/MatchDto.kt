package com.quizly.android.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateSingleMatchRequest(val difficulty: Int)

@Serializable
data class CreateDuelMatchRequest(
    val difficulty: Int,
    val opponents: List<Int>
)

@Serializable
data class CreateMatchResponse(
    @SerialName("match_id")
    val matchId: Int? = null
)

/** SPECULATIVE: not in API docs, verify against live server. */
@Serializable
data class SubmitAnswerRequest(
    @SerialName("question_id")
    val questionId: Int,
    @SerialName("answer_id")
    val answerId: Int,
    @SerialName("time_taken")
    val timeTaken: Int? = null
)

/** SPECULATIVE: not in API docs, verify against live server. */
@Serializable
data class InviteRequest(
    val friend: String,
    val message: String? = null
)
