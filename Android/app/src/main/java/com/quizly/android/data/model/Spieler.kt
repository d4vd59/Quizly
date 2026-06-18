package com.quizly.android.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Ported from Spieler.cs / spieler.py. */
@Serializable
data class Spieler(
    @SerialName("user_id")
    val userId: Int,
    val nickname: String,
    val fullname: String? = null,
    val email: String? = null,
    @SerialName("email_confirmed")
    val emailConfirmed: Boolean = false,
    @SerialName("last_login")
    val lastLogin: String? = null,
    val avatar: String? = null,
    // No confirmed source on the live API / Python model - kept nullable, only the
    // WPF client's Spieler.GesamtPunkte references a running point total.
    @SerialName("gesamt_punkte")
    val gesamtPunkte: Int? = null
)
