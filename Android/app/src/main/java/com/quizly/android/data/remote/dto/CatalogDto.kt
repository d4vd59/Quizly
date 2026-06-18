package com.quizly.android.data.remote.dto

import kotlinx.serialization.Serializable

/** Response shape for GET /gamemode is not documented - kept minimal/best-effort. */
@Serializable
data class GameModeInfo(
    val id: Int? = null,
    val name: String? = null
)

/** Response shape for GET /gamestate is not documented - kept minimal/best-effort. */
@Serializable
data class GameStateInfo(
    val id: Int? = null,
    val name: String? = null
)
