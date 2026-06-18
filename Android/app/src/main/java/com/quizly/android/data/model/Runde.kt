package com.quizly.android.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Ported from Runde.cs / runde.py. */
@Serializable
data class Runde(
    @SerialName("runde_id")
    val rundeId: Int,
    @SerialName("runden_nummer")
    val rundenNummer: Int,
    @SerialName("kategorie_id")
    val kategorieId: Int? = null,
    @SerialName("kategorie_name")
    val kategorieName: String? = null,
    @SerialName("start_zeit")
    val startZeit: String? = null,
    @SerialName("end_zeit")
    val endZeit: String? = null,
    val fragen: List<Frage> = emptyList()
)
