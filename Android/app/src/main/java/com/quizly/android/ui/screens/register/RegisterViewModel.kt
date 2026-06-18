package com.quizly.android.ui.screens.register

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.quizly.android.data.remote.ApiResult
import com.quizly.android.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class RegisterUiState(
    val name: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val passwordRepeat: String = "",
    val avatarUri: Uri? = null,
    val isLoading: Boolean = false,
    val error: String = ""
)

private val EMAIL_REGEX = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")

/** Ported from Views/RegisterView.xaml.cs's Create_Click (avatar upload is not wired to a
 * network call there either - the WPF client only keeps it as a local preview). */
class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onNameChange(value: String) { _uiState.value = _uiState.value.copy(name = value) }
    fun onUsernameChange(value: String) { _uiState.value = _uiState.value.copy(username = value) }
    fun onEmailChange(value: String) { _uiState.value = _uiState.value.copy(email = value) }
    fun onPasswordChange(value: String) { _uiState.value = _uiState.value.copy(password = value) }
    fun onPasswordRepeatChange(value: String) { _uiState.value = _uiState.value.copy(passwordRepeat = value) }
    fun onAvatarPicked(uri: Uri?) { _uiState.value = _uiState.value.copy(avatarUri = uri) }

    suspend fun register(): Boolean {
        val s = _uiState.value

        if (s.name.isBlank() || s.username.isBlank() || s.email.isBlank() || s.password.isBlank() || s.passwordRepeat.isBlank()) {
            _uiState.value = s.copy(error = "Bitte alle Felder ausfüllen.")
            return false
        }
        if (!EMAIL_REGEX.matches(s.email)) {
            _uiState.value = s.copy(error = "Bitte eine gültige E-Mail eingeben.")
            return false
        }
        if (s.password != s.passwordRepeat) {
            _uiState.value = s.copy(error = "Passwörter stimmen nicht überein.")
            return false
        }

        _uiState.value = s.copy(isLoading = true, error = "")
        val result = authRepository.signup(s.email.trim(), s.username.trim(), s.name.trim(), s.password)
        _uiState.value = _uiState.value.copy(isLoading = false)

        return when (result) {
            is ApiResult.Success -> {
                if (result.data.message == "User created successfully") {
                    true
                } else {
                    _uiState.value = _uiState.value.copy(error = result.data.message ?: "Unbekannter Registrierungsfehler")
                    false
                }
            }
            is ApiResult.HttpError -> {
                _uiState.value = _uiState.value.copy(error = result.body ?: "Registrierungsfehler (${result.code})")
                false
            }
            is ApiResult.NetworkError -> {
                _uiState.value = _uiState.value.copy(error = "Registrierungsfehler: ${result.throwable.message}")
                false
            }
        }
    }
}
