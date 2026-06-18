package com.quizly.android.ui.screens.gamemode

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quizly.android.ui.theme.QuizlyCardStrong
import com.quizly.android.ui.theme.QuizlyMuted
import com.quizly.android.ui.theme.QuizlyWhite

/** Ported from Views/GameModeSelectionView.xaml(.cs). */
@Composable
fun GameModeSelectionScreen(
    onBack: () -> Unit,
    onSingleplayer: () -> Unit,
    onMultiplayer: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(18.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = QuizlyWhite)
            }
            Text(
                "Spielmodus wählen",
                color = QuizlyWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(top = 40.dp),
            verticalArrangement = Arrangement.Center
        ) {
            GameModeCard(emoji = "👥", title = "Multiplayer", description = "Spiele gegen echte Gegner", onClick = onMultiplayer)
            GameModeCard(emoji = "🎮", title = "Singleplayer", description = "Spiele alleine und trainiere", onClick = onSingleplayer)
        }
    }
}

@Composable
private fun GameModeCard(emoji: String, title: String, description: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .background(QuizlyCardStrong, RoundedCornerShape(26.dp))
            .clickable(onClick = onClick)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(emoji, fontSize = 60.sp)
        Text(title, color = QuizlyWhite, fontSize = 24.sp, fontWeight = FontWeight.Black)
        Text(description, color = QuizlyMuted, fontSize = 13.sp, textAlign = TextAlign.Center)
    }
}
