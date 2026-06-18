package com.quizly.android.ui.screens.matchmaking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.quizly.android.data.local.SessionManager
import com.quizly.android.data.repository.MatchRepository
import com.quizly.android.data.repository.UserRepository
import com.quizly.android.ui.components.PrimaryButton
import com.quizly.android.ui.theme.QuizlyCardStrong
import com.quizly.android.ui.theme.QuizlyGreen
import com.quizly.android.ui.theme.QuizlyMuted
import com.quizly.android.ui.theme.QuizlyWhite
import kotlinx.coroutines.launch

/** Ported from Views/MatchmakingView.xaml(.cs). */
@Composable
fun MatchmakingScreen(
    userRepository: UserRepository,
    matchRepository: MatchRepository,
    sessionManager: SessionManager,
    onBack: () -> Unit,
    onOpponentFound: (opponentName: String, matchId: Int?) -> Unit
) {
    val viewModel: MatchmakingViewModel = viewModel(factory = viewModelFactory {
        initializer { MatchmakingViewModel(userRepository, matchRepository, sessionManager) }
    })
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(18.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = QuizlyWhite)
            }
            Text(
                "Gegner wird gesucht…",
                color = QuizlyWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(top = 18.dp)
                .background(QuizlyCardStrong, RoundedCornerShape(26.dp))
                .padding(18.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.size(86.dp).background(Color(0x33FFFFFF), CircleShape))
            Text("Matchmaking läuft", color = QuizlyWhite, fontSize = 22.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(top = 12.dp))
            CircularProgressIndicator(modifier = Modifier.padding(top = 12.dp))
            Text(
                "(Demo) Später echte Server-Suche.",
                color = QuizlyMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        PrimaryButton(
            text = "Gegner gefunden (Demo)",
            backgroundColor = QuizlyGreen,
            onClick = {
                scope.launch {
                    val (opponentName, matchId) = viewModel.findOpponentAndCreateMatch()
                    onOpponentFound(opponentName, matchId)
                }
            },
            modifier = Modifier.padding(top = 14.dp)
        )
    }
}
