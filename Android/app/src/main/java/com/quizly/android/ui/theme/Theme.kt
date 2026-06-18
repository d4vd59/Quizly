package com.quizly.android.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush

private val QuizlyColorScheme = darkColorScheme(
    primary = QuizlyBlueBtn,
    secondary = QuizlyGreen,
    tertiary = QuizlyGold,
    background = QuizlyBgMid,
    surface = QuizlyDarkBtn,
    onPrimary = QuizlyWhite,
    onSecondary = QuizlyWhite,
    onBackground = QuizlyWhite,
    onSurface = QuizlyWhite
)

val QuizlyBackgroundGradient = Brush.verticalGradient(
    0f to QuizlyBgTop,
    0.45f to QuizlyBgMid,
    1f to QuizlyBgBottom
)

@Composable
fun QuizlyTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = QuizlyColorScheme,
        typography = QuizlyTypography,
        shapes = QuizlyShapes,
        content = content
    )
}

@Composable
fun QuizlyBackground(content: @Composable () -> Unit) {
    androidx.compose.foundation.layout.Box(modifier = Modifier.fillMaxSize().background(QuizlyBackgroundGradient)) {
        content()
    }
}
