package com.quizly.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quizly.android.ui.theme.QuizlyGreen
import com.quizly.android.ui.theme.QuizlyWhite

/** Ported from the match card markup repeated across HomeView.xaml. */
@Composable
fun MatchCard(
    opponentName: String,
    scoreText: String,
    buttonText: String,
    avatarColor: Color,
    onClick: () -> Unit,
    onButtonClick: () -> Unit = onClick,
    enabled: Boolean = true
) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .background(Color(0xFF4D5B9E), RoundedCornerShape(20.dp))
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 8.dp)
                .size(60.dp)
                .background(avatarColor, CircleShape)
        )
        Text(
            opponentName,
            color = QuizlyWhite,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Box(
            modifier = Modifier
                .background(Color(0xFF3D4A8E), RoundedCornerShape(12.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(scoreText, color = QuizlyWhite, fontSize = 16.sp, fontWeight = FontWeight.Black)
        }
        Button(
            onClick = onButtonClick,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(containerColor = QuizlyGreen, contentColor = QuizlyWhite),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(buttonText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}
