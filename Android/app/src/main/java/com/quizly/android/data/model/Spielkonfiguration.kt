package com.quizly.android.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Ported from Spielkonfiguration.cs / spielkonfiguration.py. */
@Serializable
data class Spielkonfiguration(
    @SerialName("config_id")
    val configId: Int,
    @SerialName("config_name")
    val configName: String,
    @SerialName("anzahl_runden")
    val anzahlRunden: Int,
    @SerialName("fragen_pro_runde")
    val fragenProRunde: Int,
    @SerialName("antworten_pro_frage")
    val antwortenProFrage: Int,
    @SerialName("max_wiederholungen_frage")
    val maxWiederholungenFrage: Int,
    @SerialName("max_spiellaenge_sekunden")
    val maxSpiellaengeSekunden: Int,
    @SerialName("ist_aktiv")
    @Serializable(with = IntOrBooleanSerializer::class)
    val istAktiv: Boolean = true
)
