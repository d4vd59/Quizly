package com.quizly.android.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Ported from Frage.cs / frage.py. The live API may return category/difficulty as
 * nested objects or as plain foreign-key ids - kept nullable/best-effort either way,
 * the lenient JSON config (ignoreUnknownKeys/coerceInputValues) means a mismatch here
 * just leaves these fields null rather than crashing the whole response.
 */
@Serializable
data class Frage(
    val id: Int,
    val text: String,
    @SerialName("category")
    val categoryId: Int? = null,
    @SerialName("difficulty")
    val difficultyLevel: Int? = null,
    @SerialName("answered_correctly")
    val answeredCorrectly: Int = 0,
    @SerialName("answered_incorrectly")
    val answeredIncorrectly: Int = 0,
    val answers: List<Antwort> = emptyList()
) {
    fun isAnswerCorrect(answerId: Int): Boolean = answers.any { it.id == answerId && it.isCorrect }
}
