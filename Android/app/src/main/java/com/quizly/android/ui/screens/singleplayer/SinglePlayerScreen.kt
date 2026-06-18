package com.quizly.android.ui.screens.singleplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quizly.android.ui.components.PrimaryButton
import com.quizly.android.ui.state.GameState
import com.quizly.android.ui.theme.QuizlyGold
import com.quizly.android.ui.theme.QuizlyGreen
import com.quizly.android.ui.theme.QuizlyMuted
import com.quizly.android.ui.theme.QuizlyWhite

/** Ported from Views/SinglePlayerView.xaml(.cs). */
@Composable
fun SinglePlayerScreen(
    gameState: GameState,
    onBack: () -> Unit,
    onPlay: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = QuizlyWhite)
        }

        Text(
            text = if (gameState.currentRound == 0) "Singleplayer Training" else "Runde ${gameState.currentRound} abgeschlossen!",
            color = QuizlyGold,
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(45.dp).background(com.quizly.android.ui.theme.QuizlyCyan, CircleShape))
                Text("Du", color = QuizlyWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Text(
                "${gameState.playerScore} / ${gameState.totalRounds}",
                color = QuizlyWhite,
                fontSize = 36.sp,
                fontWeight = FontWeight.Black
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🎯", fontSize = 32.sp)
                Text("Ziel", color = QuizlyWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            items((1..gameState.totalRounds).toList()) { round ->
                RoundRow(round = round, done = round <= gameState.currentRound)
            }
        }

        PrimaryButton(text = "Spielen", backgroundColor = QuizlyGreen, onClick = onPlay, modifier = Modifier.padding(top = 10.dp))
    }
}

@Composable
private fun RoundRow(round: Int, done: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(if (done) QuizlyGreen else Color(0xFF1A2A5E), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("$round", color = QuizlyWhite, fontSize = 18.sp, fontWeight = FontWeight.Black)
        }
        Text("Runde $round", color = QuizlyMuted, fontSize = 14.sp, modifier = Modifier.padding(start = 12.dp))
    }
}
