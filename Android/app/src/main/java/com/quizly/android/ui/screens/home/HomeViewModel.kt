package com.quizly.android.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quizly.android.data.local.SessionManager
import com.quizly.android.data.model.Match
import com.quizly.android.data.model.Spieler
import com.quizly.android.data.remote.ApiResult
import com.quizly.android.data.repository.AuthRepository
import com.quizly.android.data.repository.MatchRepository
import com.quizly.android.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val currentUser: Spieler? = null,
    val runningMatches: List<Match> = emptyList(),
    val endedMatches: List<Match> = emptyList(),
    val isLoading: Boolean = false,
    val showSettings: Boolean = false
)

/** Ported from Views/HomeView.xaml(.cs). */
class HomeViewModel(
    private val userRepository: UserRepository,
    private val matchRepository: MatchRepository,
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    private fun load() {
        val userIdentifier = sessionManager.currentUser.value?.userIdentifier ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val userResult = userRepository.getUser(userIdentifier)
            val runningResult = matchRepository.getUserMatches(userIdentifier, "running")
            val endedResult = matchRepository.getUserMatches(userIdentifier, "ended")

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                currentUser = (userResult as? ApiResult.Success)?.data,
                runningMatches = (runningResult as? ApiResult.Success)?.data ?: emptyList(),
                endedMatches = (endedResult as? ApiResult.Success)?.data ?: emptyList()
            )
        }
    }

    fun openSettings() { _uiState.value = _uiState.value.copy(showSettings = true) }
    fun closeSettings() { _uiState.value = _uiState.value.copy(showSettings = false) }

    suspend fun logout() {
        sessionManager.currentUser.value?.userIdentifier?.let { authRepository.signout(it) }
    }
}
