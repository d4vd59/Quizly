package com.quizly.android.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.quizly.android.data.repository.AuthRepository
import com.quizly.android.ui.components.AppTextField
import com.quizly.android.ui.components.ErrorText
import com.quizly.android.ui.components.PrimaryButton
import com.quizly.android.ui.components.SecondaryButton
import com.quizly.android.ui.theme.QuizlyGreen
import com.quizly.android.ui.theme.QuizlyWhite
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    authRepository: AuthRepository,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onGuest: () -> Unit
) {
    val viewModel: LoginViewModel = viewModel(factory = viewModelFactory {
        initializer { LoginViewModel(authRepository) }
    })
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(18.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(140.dp)
                .background(Color(0x22FFFFFF), CircleShape)
        )
        Text(
            "Quizly",
            color = QuizlyWhite,
            fontSize = 44.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        AppTextField(value = uiState.user, onValueChange = viewModel::onUserChange, label = "E-Mail")
        AppTextField(value = uiState.password, onValueChange = viewModel::onPasswordChange, label = "Passwort", isPassword = true)

        ErrorText(uiState.error)

        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally).padding(8.dp))
        } else {
            PrimaryButton(
                text = "Einloggen",
                backgroundColor = QuizlyGreen,
                onClick = {
                    scope.launch {
                        if (viewModel.login()) onLoginSuccess()
                    }
                }
            )
        }

        Text(
            "ODER",
            color = com.quizly.android.ui.theme.QuizlyMuted,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        SecondaryButton(text = "Neues Spielerkonto", onClick = onNavigateToRegister)
        SecondaryButton(text = "Als Gast spielen", onClick = onGuest)
    }
}
