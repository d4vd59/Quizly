package com.quizly.android.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.quizly.android.data.local.SessionManager
import com.quizly.android.data.model.Match
import com.quizly.android.data.repository.AuthRepository
import com.quizly.android.data.repository.MatchRepository
import com.quizly.android.data.repository.UserRepository
import com.quizly.android.ui.components.MatchCard
import com.quizly.android.ui.theme.QuizlyCardStrong
import com.quizly.android.ui.theme.QuizlyCyan
import com.quizly.android.ui.theme.QuizlyGreen
import com.quizly.android.ui.theme.QuizlyWhite
import kotlinx.coroutines.launch

/**
 * Ported from the hardcoded match cards in Views/HomeView.xaml. The WPF source never binds
 * these to real data - they're static demo cards always shown, so this fallback content
 * keeps Android's Home screen visually identical when no real matches are available (guest
 * mode, or the live API unreachable) instead of leaving the sections empty.
 */
private data class DemoMatchCard(val name: String, val score: String, val avatarColor: Color)

private val DEMO_YOUR_TURN = listOf(
    DemoMatchCard("Franzi.250303", "1 - 4", Color(0xFFFF8C42)),
    DemoMatchCard("Dickie1202", "0 - 4", Color(0xFFFF6B9D)),
    DemoMatchCard("Beliebiger Spieler", "0 - 0", Color(0xFF7B68EE))
)

private val DEMO_OPPONENT_TURN = listOf(
    DemoMatchCard("guest#97821195", "2 - 1", QuizlyCyan),
    DemoMatchCard("Fleetwater", "1 - 2", Color(0xFFFF8C42))
)

private val DEMO_FINISHED = listOf(
    DemoMatchCard("Player123", "12 - 10", Color(0xFF9B59B6))
)

@Composable
fun HomeScreen(
    userRepository: UserRepository,
    matchRepository: MatchRepository,
    authRepository: AuthRepository,
    sessionManager: SessionManager,
    onNewGame: () -> Unit,
    onPlayMatch: (Match) -> Unit,
    onViewResult: (Match) -> Unit,
    onLoggedOut: () -> Unit
) {
    val viewModel: HomeViewModel = viewModel(factory = viewModelFactory {
        initializer { HomeViewModel(userRepository, matchRepository, authRepository, sessionManager) }
    })
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = viewModel::openSettings, modifier = Modifier.align(Alignment.CenterStart)) {
                    Icon(Icons.Filled.Settings, contentDescription = "Settings", tint = QuizlyWhite)
                }
                Text(
                    "Quizly",
                    color = QuizlyWhite,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(50.dp).background(QuizlyCyan, CircleShape))
                Text(
                    uiState.currentUser?.nickname ?: "Gast",
                    color = QuizlyWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

            Button(
                onClick = onNewGame,
                colors = ButtonDefaults.buttonColors(containerColor = QuizlyGreen, contentColor = QuizlyWhite),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Text("Neues Spiel starten  +", fontWeight = FontWeight.Bold)
            }

            Text("Du bist dran", color = Color(0xFF7DB8FF), fontSize = 18.sp, fontWeight = FontWeight.Bold)
            LazyRow(contentPadding = PaddingValues(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (uiState.runningMatches.isNotEmpty()) {
                    items(uiState.runningMatches) { match ->
                        MatchCard(
                            opponentName = match.spieler.firstOrNull()?.nickname ?: "Spieler",
                            scoreText = "-",
                            buttonText = "Spielen",
                            avatarColor = QuizlyCyan,
                            onClick = { onPlayMatch(match) }
                        )
                    }
                } else {
                    items(DEMO_YOUR_TURN) { demo ->
                        MatchCard(
                            opponentName = demo.name,
                            scoreText = demo.score,
                            buttonText = "Spielen",
                            avatarColor = demo.avatarColor,
                            onClick = onNewGame
                        )
                    }
                }
            }

            Text("Spielt", color = Color(0xFF7DB8FF), fontSize = 18.sp, fontWeight = FontWeight.Bold)
            LazyRow(contentPadding = PaddingValues(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(DEMO_OPPONENT_TURN) { demo ->
                    MatchCard(
                        opponentName = demo.name,
                        scoreText = demo.score,
                        buttonText = "Spielt",
                        avatarColor = demo.avatarColor,
                        onClick = {},
                        enabled = false
                    )
                }
            }

            Text("Beendet", color = Color(0xFF7DB8FF), fontSize = 18.sp, fontWeight = FontWeight.Bold)
            LazyRow(contentPadding = PaddingValues(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (uiState.endedMatches.isNotEmpty()) {
                    items(uiState.endedMatches) { match ->
                        MatchCard(
                            opponentName = match.spieler.firstOrNull()?.nickname ?: "Spieler",
                            scoreText = "-",
                            buttonText = "Ansehen",
                            avatarColor = Color(0xFF9B59B6),
                            onClick = { onViewResult(match) }
                        )
                    }
                } else {
                    items(DEMO_FINISHED) { demo ->
                        MatchCard(
                            opponentName = demo.name,
                            scoreText = demo.score,
                            buttonText = "Ansehen",
                            avatarColor = demo.avatarColor,
                            onClick = onNewGame
                        )
                    }
                }
            }
        }

        if (uiState.showSettings) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color(0xCC000000)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .width(250.dp)
                        .background(QuizlyCardStrong, RoundedCornerShape(20.dp))
                        .padding(20.dp)
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "⚙ Einstellungen",
                            color = QuizlyWhite,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.align(Alignment.Center)
                        )
                        IconButton(onClick = viewModel::closeSettings, modifier = Modifier.align(Alignment.TopEnd)) {
                            Icon(Icons.Filled.Close, contentDescription = "Close", tint = QuizlyWhite)
                        }
                    }
                    Button(
                        onClick = {
                            scope.launch {
                                viewModel.logout()
                                onLoggedOut()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE74C3C), contentColor = QuizlyWhite),
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                    ) {
                        Text("Ausloggen", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
