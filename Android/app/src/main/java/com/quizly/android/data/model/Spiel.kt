package com.quizly.android.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Ported from Spiel.cs / spiel.py. */
@Serializable
data class Spiel(
    @SerialName("spiel_id")
    val spielId: Int,
    val spielmodus: Spielmodus? = null,
    val status: Spielstatus? = null,
    @SerialName("config_id")
    val configId: Int? = null,
    @SerialName("schwierigkeitsgrad_level")
    val schwierigkeitsgradLevel: Int? = null,
    @SerialName("start_zeit")
    val startZeit: String? = null,
    @SerialName("end_zeit")
    val endZeit: String? = null,
    val spieler: List<Spieler> = emptyList(),
    val runden: List<Runde> = emptyList()
)
