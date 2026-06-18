package com.quizly.android.ui.state

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Ported from GameState.cs + the StartSinglePlayer/StartMultiplayer/StartNewGame methods
 * on MainWindow.xaml.cs. Scoped to the NavHost's own back stack entry so every gameplay
 * screen (SinglePlayer/DuelOverview/CategoryPick/Question/Result) shares one instance,
 * replacing MainWindow.CurrentGameState as the single source of truth.
 */
class GameSessionViewModel : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state.asStateFlow()

    fun reset() {
        _state.value = GameState()
    }

    fun startSinglePlayer() {
        _state.value = GameState(isSinglePlayer = true, opponentName = "Singleplayer")
    }

    fun startMultiplayer(opponentName: String) {
        _state.value = GameState(isSinglePlayer = false, opponentName = opponentName)
    }

    fun setMatchId(matchId: Int) {
        _state.value = _state.value.copy(matchId = matchId)
    }

    fun advanceRound(playerPoints: Int = 0, opponentPoints: Int = 0) {
        val current = _state.value
        _state.value = current.copy(
            currentRound = current.currentRound + 1,
            playerScore = current.playerScore + playerPoints,
            opponentScore = current.opponentScore + opponentPoints
        )
    }

    fun isGameFinished(): Boolean = _state.value.isGameFinished()
}
