package com.quizly.android.ui.screens.result

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quizly.android.ui.components.PrimaryButton
import com.quizly.android.ui.state.GameState
import com.quizly.android.ui.theme.QuizlyCyan
import com.quizly.android.ui.theme.QuizlyGold
import com.quizly.android.ui.theme.QuizlyGreen
import com.quizly.android.ui.theme.QuizlyOrange
import com.quizly.android.ui.theme.QuizlyWhite

/** Ported from Views/ResultView.xaml(.cs). The WPF source never calls InitializeComponent()
 * here, so the original screen renders blank - this implementation fills it in with real
 * GameState-driven content instead of reproducing that bug. */
@Composable
fun ResultScreen(
    gameState: GameState,
    onBack: () -> Unit,
    onPlayAgain: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(18.dp)) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = QuizlyWhite)
        }

        Text(
            if (gameState.playerScore >= gameState.opponentScore) "Du gewinnst!" else "Du bist dran",
            color = QuizlyGold,
            fontSize = 36.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(90.dp).background(QuizlyCyan, CircleShape))
                Text("Du", color = QuizlyWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
            Text(
                "${gameState.playerScore}  -  ${gameState.opponentScore}",
                color = QuizlyWhite,
                fontSize = 64.sp,
                fontWeight = FontWeight.Black
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(90.dp).background(QuizlyOrange, CircleShape))
                Text(
                    gameState.opponentName.ifBlank { "Gegner" },
                    color = QuizlyWhite,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Box(modifier = Modifier.fillMaxSize().weight(1f))

        PrimaryButton(text = "Nochmal Spielen", backgroundColor = QuizlyGreen, onClick = onPlayAgain)
    }
}
