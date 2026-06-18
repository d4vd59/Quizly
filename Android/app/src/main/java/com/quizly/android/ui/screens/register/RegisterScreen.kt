package com.quizly.android.ui.screens.register

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.compose.AsyncImage
import com.quizly.android.data.repository.AuthRepository
import com.quizly.android.ui.components.AppTextField
import com.quizly.android.ui.components.ErrorText
import com.quizly.android.ui.components.PrimaryButton
import com.quizly.android.ui.theme.QuizlyDarkBtn
import com.quizly.android.ui.theme.QuizlyGreen
import com.quizly.android.ui.theme.QuizlyMuted
import com.quizly.android.ui.theme.QuizlyWhite
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    authRepository: AuthRepository,
    onRegisterSuccess: () -> Unit,
    onBack: () -> Unit,
    onGoLogin: () -> Unit
) {
    val viewModel: RegisterViewModel = viewModel(factory = viewModelFactory {
        initializer { RegisterViewModel(authRepository) }
    })
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    val pickAvatarLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> viewModel.onAvatarPicked(uri) }

    Column(modifier = Modifier.fillMaxSize().padding(18.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = QuizlyWhite)
            }
            Text(
                "Konto erstellen",
                color = QuizlyWhite,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(Color(0x22FFFFFF))
                    .clickable { pickAvatarLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                contentAlignment = Alignment.Center
            ) {
                if (uiState.avatarUri != null) {
                    AsyncImage(
                        model = uiState.avatarUri,
                        contentDescription = "Avatar",
                        modifier = Modifier.size(96.dp).clip(CircleShape)
                    )
                }
            }
            Text("Avatar auswählen", color = QuizlyMuted)
        }

        AppTextField(value = uiState.name, onValueChange = viewModel::onNameChange, label = "Name")
        AppTextField(value = uiState.username, onValueChange = viewModel::onUsernameChange, label = "Benutzername")
        AppTextField(value = uiState.email, onValueChange = viewModel::onEmailChange, label = "E-Mail")
        AppTextField(value = uiState.password, onValueChange = viewModel::onPasswordChange, label = "Passwort", isPassword = true)
        AppTextField(value = uiState.passwordRepeat, onValueChange = viewModel::onPasswordRepeatChange, label = "Passwort wiederholen", isPassword = true)

        ErrorText(uiState.error)

        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(8.dp))
        } else {
            PrimaryButton(
                text = "Konto anlegen",
                backgroundColor = QuizlyGreen,
                onClick = {
                    scope.launch {
                        if (viewModel.register()) onRegisterSuccess()
                    }
                }
            )
            PrimaryButton(text = "Schon ein Konto? Einloggen", backgroundColor = QuizlyDarkBtn, onClick = onGoLogin)
        }
    }
}
