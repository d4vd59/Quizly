package com.quizly.android.ui.screens.question

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.quizly.android.data.repository.MatchRepository
import com.quizly.android.ui.theme.QuizlyWhite
import kotlinx.coroutines.launch

/** Ported from Views/QuestionView.xaml(.cs). */
@Composable
fun QuestionScreen(
    matchRepository: MatchRepository,
    matchId: Int?,
    roundNumber: Int,
    categoryName: String,
    onAnswered: (correct: Boolean) -> Unit
) {
    val viewModel: QuestionViewModel = viewModel(factory = viewModelFactory {
        initializer { QuestionViewModel(matchRepository, matchId, roundNumber) }
    })
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(18.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0x66000000), RoundedCornerShape(20.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(categoryName, color = QuizlyWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            else -> {
                val question = uiState.question!!
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 16.dp)
                        .background(Color(0xEFFFFFFF), RoundedCornerShape(26.dp))
                        .padding(horizontal = 24.dp, vertical = 20.dp)
                ) {
                    Text(
                        question.text,
                        color = Color(0xFF2B2F6D),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.fillMaxSize()) {
                    items(question.answers) { answer ->
                        Button(
                            onClick = {
                                scope.launch { onAnswered(viewModel.answer(answer.id)) }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4D5B9E), contentColor = QuizlyWhite),
                            shape = RoundedCornerShape(28.dp),
                            modifier = Modifier.padding(8.dp).height(92.dp).fillMaxWidth()
                        ) {
                            Text(answer.text, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
