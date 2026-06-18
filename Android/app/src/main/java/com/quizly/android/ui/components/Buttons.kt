package com.quizly.android.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.quizly.android.ui.theme.QuizlyWhite

/** Ported from App.xaml's PillButton style. */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = androidx.compose.material3.MaterialTheme.colorScheme.secondary,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth().height(58.dp),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor, contentColor = QuizlyWhite),
        shape = androidx.compose.material3.MaterialTheme.shapes.large
    ) {
        Text(text, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
    }
}

/** Ported from App.xaml's outlined PillButton variant (transparent background, white border). */
@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth().height(58.dp),
        border = BorderStroke(2.dp, QuizlyWhite),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = QuizlyWhite),
        shape = androidx.compose.material3.MaterialTheme.shapes.large
    ) {
        Text(text, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
    }
}
