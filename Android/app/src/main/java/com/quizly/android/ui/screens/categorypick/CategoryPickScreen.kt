package com.quizly.android.ui.screens.categorypick

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.quizly.android.data.repository.CatalogRepository
import com.quizly.android.data.repository.MatchRepository
import com.quizly.android.data.repository.UserRepository
import com.quizly.android.ui.theme.QuizlyCyan
import com.quizly.android.ui.theme.QuizlyWhite

/** Ported from Views/CategoryPickView.xaml(.cs) - back navigation intentionally disabled,
 * matching the source where the back button is hidden and its handler left empty. */
@Composable
fun CategoryPickScreen(
    catalogRepository: CatalogRepository,
    matchRepository: MatchRepository,
    userRepository: UserRepository,
    sessionManager: SessionManager,
    isSinglePlayer: Boolean,
    existingMatchId: Int?,
    onMatchCreated: (Int) -> Unit,
    onCategorySelected: (CategoryUi) -> Unit
) {
    val viewModel: CategoryPickViewModel = viewModel(factory = viewModelFactory {
        initializer {
            CategoryPickViewModel(catalogRepository, matchRepository, userRepository, sessionManager, isSinglePlayer, existingMatchId, onMatchCreated)
        }
    })
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(18.dp)) {
        Text(
            "Wähle eine Kategorie",
            color = QuizlyWhite,
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0x66000000), RoundedCornerShape(20.dp))
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(32.dp).background(QuizlyCyan, CircleShape))
            Text(
                if (uiState.isPlayerTurn) "Du bist dran!" else "Gegner ist dran!",
                color = QuizlyWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 12.dp)
            )
        }

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 20.dp)) {
                items(uiState.categories) { category ->
                    CategoryCard(category = category, onClick = { onCategorySelected(category) })
                }
            }
        }
    }
}

@Composable
private fun CategoryCard(category: CategoryUi, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .padding(bottom = 16.dp)
            .background(Color(0x33FFFFFF), RoundedCornerShape(26.dp))
            .clickable(onClick = onClick)
            .padding(20.dp)
    ) {
        Column(modifier = Modifier.align(Alignment.CenterStart)) {
            Text(category.name, color = QuizlyWhite, fontSize = 24.sp, fontWeight = FontWeight.Black)
            Text(category.description, color = Color(0xCCFFFFFF), fontSize = 14.sp, modifier = Modifier.padding(top = 4.dp))
        }
        Text(category.emoji, fontSize = 32.sp, modifier = Modifier.align(Alignment.CenterEnd))
    }
}
