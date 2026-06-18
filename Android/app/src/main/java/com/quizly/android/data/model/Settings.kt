package com.quizly.android.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Ported from settings.py (no C# equivalent) - response shape of GET /setting. */
@Serializable
data class Settings(
    @SerialName("turns_per_game")
    val turnsPerGame: Int = 6,
    @SerialName("questions_per_turn")
    val questionsPerTurn: Int = 3,
    @SerialName("answers_per_question")
    val answersPerQuestion: Int = 4,
    @SerialName("max_time_game")
    val maxTimeGame: Int = 864000,
    @SerialName("max_time_turn")
    val maxTimeTurn: Int = 432000,
    @SerialName("max_time_question")
    val maxTimeQuestion: Int = 30,
    @SerialName("points_per_question")
    val pointsPerQuestion: Int = 1,
    @SerialName("question_repetition_per_game")
    val questionRepetitionPerGame: Int = 1
)
