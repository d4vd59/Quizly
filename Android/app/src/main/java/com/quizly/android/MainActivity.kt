package com.quizly.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.quizly.android.ui.navigation.QuizlyApp
import com.quizly.android.ui.theme.QuizlyBackground
import com.quizly.android.ui.theme.QuizlyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val container = (application as QuizlyApplication).container

        setContent {
            QuizlyTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    QuizlyBackground {
                        QuizlyApp(container)
                    }
                }
            }
        }
    }
}
