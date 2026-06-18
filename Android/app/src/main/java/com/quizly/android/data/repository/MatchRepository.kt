package com.quizly.android.data.repository

import com.quizly.android.data.model.Frage
import com.quizly.android.data.model.Match
import com.quizly.android.data.remote.ApiResult
import com.quizly.android.data.remote.ApiService
import com.quizly.android.data.remote.dto.CreateDuelMatchRequest
import com.quizly.android.data.remote.dto.CreateMatchResponse
import com.quizly.android.data.remote.dto.CreateSingleMatchRequest
import com.quizly.android.data.remote.dto.GenericMessageResponse
import com.quizly.android.data.remote.dto.SubmitAnswerRequest

/**
 * Ported from api_client.py get_user_matches/create_single_match/create_duel_match (confirmed)
 * plus the speculative match/{id}, round/questions, answer, end_turn methods.
 */
class MatchRepository(private val api: ApiService) {

    suspend fun getUserMatches(user: String, matchType: String = "all", limit: Int? = null): ApiResult<List<Match>> =
        safeCall {
            if (matchType == "all") api.getUserMatches(user, limit) else api.getUserMatchesByType(user, matchType, limit)
        }

    suspend fun createSingleMatch(userId: Int, difficulty: Int): ApiResult<CreateMatchResponse> =
        safeCall { api.createSingleMatch(userId, CreateSingleMatchRequest(difficulty)) }

    suspend fun createDuelMatch(userId: Int, difficulty: Int, opponentIds: List<Int>): ApiResult<CreateMatchResponse> =
        safeCall { api.createDuelMatch(userId, CreateDuelMatchRequest(difficulty, opponentIds)) }

    // ===== SPECULATIVE: not in API docs, verify against live server =====

    suspend fun getMatchDetails(matchId: Int): ApiResult<Match> = safeCall { api.getMatchDetails(matchId) }

    suspend fun getRoundQuestions(matchId: Int, roundNumber: Int): ApiResult<List<Frage>> =
        safeCall { api.getRoundQuestions(matchId, roundNumber) }

    suspend fun submitAnswer(matchId: Int, questionId: Int, answerId: Int, timeTaken: Int? = null): ApiResult<GenericMessageResponse> =
        safeCall { api.submitAnswer(matchId, SubmitAnswerRequest(questionId, answerId, timeTaken)) }

    suspend fun endTurn(matchId: Int): ApiResult<GenericMessageResponse> = safeCall { api.endTurn(matchId) }
}
