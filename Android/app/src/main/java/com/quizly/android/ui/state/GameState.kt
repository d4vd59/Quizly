package com.quizly.android.ui.state

/** Ported from GameState.cs - transient UI state shared across the gameplay screens. */
data class GameState(
    val currentRound: Int = 0,
    val totalRounds: Int = 6,
    val playerScore: Int = 0,
    val opponentScore: Int = 0,
    val opponentName: String = "",
    val isSinglePlayer: Boolean = false,
    val matchId: Int? = null
) {
    fun isGameFinished(): Boolean = currentRound >= totalRounds
}
