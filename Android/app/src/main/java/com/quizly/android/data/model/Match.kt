package com.quizly.android.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * SPECULATIVE: not in API docs, verify against live server.
 * Inferred shape for GET /match/{id} and GET /match/{id}/round/{n}/questions, modeled
 * after how DuelOverviewView/QuestionView in the WPF client consume match data.
 */
@Serializable
data class Match(
    @SerialName("match_id")
    val matchId: Int,
    val spielmodus: Spielmodus? = null,
    val status: Spielstatus? = null,
    @SerialName("difficulty")
    val difficultyLevel: Int? = null,
    val spieler: List<Spieler> = emptyList(),
    val runden: List<Runde> = emptyList()
)
