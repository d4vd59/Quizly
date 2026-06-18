package com.quizly.android.ui.screens.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quizly.android.data.local.SessionManager
import com.quizly.android.data.model.LeaderboardEntry
import com.quizly.android.data.model.Spieler
import com.quizly.android.data.remote.ApiResult
import com.quizly.android.data.repository.SocialRepository
import com.quizly.android.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class FriendsUiState(
    val currentUserNickname: String = "Gast",
    val searchQuery: String = "",
    val searchResults: List<Spieler> = emptyList(),
    val hasSearched: Boolean = false,
    val showInviteOverlay: Boolean = false,
    val inviteTarget: Spieler? = null,
    val inviteStatusMessage: String? = null,
    val leaderboard: List<LeaderboardEntry> = emptyList(),
    val leaderboardUnavailable: Boolean = false
)

/** Ported from Views/FriendsView.xaml(.cs). Search is client-filtered against the confirmed
 * GET /user endpoint (the WPF source only shows a MessageBox stub, "// TODO: Benutzer
 * suchen") - inviting is speculative and guarded, matching the source's own "(Demo)" label. */
class FriendsViewModel(
    private val userRepository: UserRepository,
    private val socialRepository: SocialRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(FriendsUiState())
    val uiState: StateFlow<FriendsUiState> = _uiState.asStateFlow()

    init {
        sessionManager.currentUser.value?.userIdentifier?.let { identifier ->
            viewModelScope.launch {
                val result = userRepository.getUser(identifier)
                (result as? ApiResult.Success)?.data?.nickname?.let { nickname ->
                    _uiState.value = _uiState.value.copy(currentUserNickname = nickname)
                }
            }
        }
        viewModelScope.launch {
            val result = socialRepository.getLeaderboard(limit = 5)
            _uiState.value = when (result) {
                is ApiResult.Success -> _uiState.value.copy(leaderboard = result.data)
                else -> _uiState.value.copy(leaderboardUnavailable = true)
            }
        }
    }

    fun onSearchQueryChange(value: String) {
        _uiState.value = _uiState.value.copy(searchQuery = value)
    }

    fun search() {
        val query = _uiState.value.searchQuery.trim()
        if (query.isEmpty()) return

        viewModelScope.launch {
            val result = userRepository.getUsers()
            val matches = (result as? ApiResult.Success)?.data.orEmpty()
                .filter { it.nickname.contains(query, ignoreCase = true) }
            _uiState.value = _uiState.value.copy(searchResults = matches, hasSearched = true)
        }
    }

    /** [user] is null when opened generically via "Freunde einladen" rather than from a
     * specific search result row - mirrors WPF's Invite_Click, which opens the same overlay
     * either way. */
    fun openInvite(user: Spieler? = null) {
        _uiState.value = _uiState.value.copy(showInviteOverlay = true, inviteTarget = user, inviteStatusMessage = null)
    }

    fun closeInvite() {
        _uiState.value = _uiState.value.copy(showInviteOverlay = false, inviteTarget = null, inviteStatusMessage = null)
    }

    fun sendInvite() {
        val friendName = _uiState.value.inviteTarget?.nickname ?: _uiState.value.searchQuery.trim()
        if (friendName.isEmpty()) return

        viewModelScope.launch {
            val result = socialRepository.inviteFriend(friendName)
            _uiState.value = when (result) {
                is ApiResult.Success -> _uiState.value.copy(showInviteOverlay = false, inviteTarget = null)
                else -> _uiState.value.copy(inviteStatusMessage = "Einladungen derzeit nicht verfügbar")
            }
        }
    }
}
