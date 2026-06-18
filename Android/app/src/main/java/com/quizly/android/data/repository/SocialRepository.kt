package com.quizly.android.data.repository

import com.quizly.android.data.model.Invite
import com.quizly.android.data.model.LeaderboardEntry
import com.quizly.android.data.remote.ApiResult
import com.quizly.android.data.remote.ApiService
import com.quizly.android.data.remote.dto.GenericMessageResponse
import com.quizly.android.data.remote.dto.InviteRequest

/**
 * SPECULATIVE: not in API docs, verify against live server.
 * Ported from api_client.py invite_friend/accept_invite/get_invites/get_leaderboard.
 */
class SocialRepository(private val api: ApiService) {

    suspend fun inviteFriend(friend: String, message: String? = null): ApiResult<Invite> =
        safeCall { api.inviteFriend(InviteRequest(friend, message)) }

    suspend fun acceptInvite(inviteId: Int): ApiResult<GenericMessageResponse> =
        safeCall { api.acceptInvite(inviteId) }

    suspend fun getInvites(): ApiResult<List<Invite>> = safeCall { api.getInvites() }

    suspend fun getLeaderboard(limit: Int = 10, period: String = "all"): ApiResult<List<LeaderboardEntry>> =
        safeCall { api.getLeaderboard(limit, period) }
}
