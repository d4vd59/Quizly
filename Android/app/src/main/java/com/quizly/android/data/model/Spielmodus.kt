package com.quizly.android.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Ported from enums/Spielmodus.cs. The Python model (spiel.py) stores the equivalent
 * as the raw strings 'einzelmodus'/'duellmodus' - kept as the @SerialName here since
 * the Python wrapper is the closer mirror of the live API's actual field values.
 */
@Serializable
enum class Spielmodus {
    @SerialName("einzelmodus")
    EINZELSPIELER,
    @SerialName("duellmodus")
    MEHRSPIELER
}
