package com.quizly.android.ui.screens.login

import androidx.lifecycle.ViewModel
import com.quizly.android.data.remote.ApiResult
import com.quizly.android.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class LoginUiState(
    val user: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String = ""
)

/** Ported from Views/LoginView.xaml.cs's Login_Click. */
class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onUserChange(value: String) {
        _uiState.value = _uiState.value.copy(user = value)
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(password = value)
    }

    suspend fun login(): Boolean {
        val state = _uiState.value
        if (state.user.isBlank() || state.password.isBlank()) {
            _uiState.value = state.copy(error = "Bitte E-Mail und Passwort eingeben.")
            return false
        }

        _uiState.value = state.copy(isLoading = true, error = "")
        val result = authRepository.signin(state.user.trim(), state.password)
        _uiState.value = _uiState.value.copy(isLoading = false)

        return when (result) {
            is ApiResult.Success -> {
                if (result.data.authToken != null) {
                    true
                } else {
                    _uiState.value = _uiState.value.copy(error = "Unbekannter Fehler")
                    false
                }
            }
            is ApiResult.HttpError -> {
                _uiState.value = _uiState.value.copy(error = result.body ?: "Login-Fehler (${result.code})")
                false
            }
            is ApiResult.NetworkError -> {
                _uiState.value = _uiState.value.copy(error = "Login-Fehler: ${result.throwable.message}")
                false
            }
        }
    }
}
