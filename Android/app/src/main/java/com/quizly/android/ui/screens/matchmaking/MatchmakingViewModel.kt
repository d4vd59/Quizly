package com.quizly.android.ui.screens.matchmaking

import androidx.lifecycle.ViewModel
import com.quizly.android.data.local.SessionManager
import com.quizly.android.data.remote.ApiResult
import com.quizly.android.data.repository.MatchRepository
import com.quizly.android.data.repository.UserRepository

/** Ported from Views/MatchmakingView.xaml.cs's Found_Click - the WPF source itself only
 * has a demo "Gegner gefunden" button with a hardcoded opponent name; this attempts a real
 * GET /user/random + POST .../match/duel and falls back to the same demo name on failure. */
class MatchmakingViewModel(
    private val userRepository: UserRepository,
    private val matchRepository: MatchRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    suspend fun findOpponentAndCreateMatch(): Pair<String, Int?> {
        val opponentResult = userRepository.getRandomUser()
        val opponent = (opponentResult as? ApiResult.Success)?.data
        val opponentName = opponent?.nickname ?: "Gegner123"

        val identifier = sessionManager.currentUser.value?.userIdentifier
        if (identifier == null || opponent == null) return opponentName to null

        val userResult = userRepository.getUser(identifier)
        val userId = (userResult as? ApiResult.Success)?.data?.userId ?: return opponentName to null

        val matchResult = matchRepository.createDuelMatch(userId, difficulty = 1, opponentIds = listOf(opponent.userId))
        val matchId = (matchResult as? ApiResult.Success)?.data?.matchId

        return opponentName to matchId
    }
}
