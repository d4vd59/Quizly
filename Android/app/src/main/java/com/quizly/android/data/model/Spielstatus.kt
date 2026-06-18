package com.quizly.android.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Ported from enums/Spielstatus.cs, @SerialName values mirror spiel.py's raw strings. */
@Serializable
enum class Spielstatus {
    @SerialName("laufend")
    LAUFEND,
    @SerialName("beendet")
    BEENDET,
    @SerialName("abgebrochen")
    ABGEBROCHEN
}
