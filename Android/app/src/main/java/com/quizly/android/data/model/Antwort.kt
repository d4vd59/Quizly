package com.quizly.android.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

/**
 * Ported from Antwort.cs (bool IstRichtig) / antwort.py (int correct_answer, 0/1).
 * [IntOrBooleanSerializer] tolerates either shape since the live API's exact
 * representation is unconfirmed.
 */
@Serializable
data class Antwort(
    val id: Int,
    val text: String,
    @SerialName("correct_answer")
    @Serializable(with = IntOrBooleanSerializer::class)
    val isCorrect: Boolean = false
)
