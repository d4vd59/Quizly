package com.quizly.android.ui.screens.friends

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.quizly.android.data.local.SessionManager
import com.quizly.android.data.model.Spieler
import com.quizly.android.data.repository.SocialRepository
import com.quizly.android.data.repository.UserRepository
import com.quizly.android.ui.components.PrimaryButton
import com.quizly.android.ui.theme.QuizlyBlueBtn
import com.quizly.android.ui.theme.QuizlyCardStrong
import com.quizly.android.ui.theme.QuizlyCyan
import com.quizly.android.ui.theme.QuizlyDarkBtn
import com.quizly.android.ui.theme.QuizlyMuted
import com.quizly.android.ui.theme.QuizlyWhite

/** Ported from Views/FriendsView.xaml(.cs). */
@Composable
fun FriendsScreen(
    userRepository: UserRepository,
    socialRepository: SocialRepository,
    sessionManager: SessionManager
) {
    val viewModel: FriendsViewModel = viewModel(factory = viewModelFactory {
        initializer { FriendsViewModel(userRepository, socialRepository, sessionManager) }
    })
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(18.dp)) {
            Text("Freunde", color = QuizlyWhite, fontSize = 24.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(bottom = 14.dp))

            Column(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(70.dp).background(QuizlyCyan, CircleShape))
                Text(uiState.currentUserNickname, color = QuizlyWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 10.dp))
            }

            LeaderboardSection(entries = uiState.leaderboard, unavailable = uiState.leaderboardUnavailable)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x33FFFFFF), RoundedCornerShape(22.dp))
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = uiState.searchQuery,
                    onValueChange = viewModel::onSearchQueryChange,
                    placeholder = { Text("Freunde suchen", color = Color(0xAAFFFFFF)) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = QuizlyWhite,
                        unfocusedTextColor = QuizlyWhite,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                IconButton(onClick = viewModel::search) {
                    Icon(Icons.Filled.Search, contentDescription = "Search", tint = QuizlyWhite)
                }
            }

            PrimaryButton(
                text = "Freunde einladen",
                backgroundColor = QuizlyBlueBtn,
                onClick = { viewModel.openInvite() },
                modifier = Modifier.padding(top = 14.dp)
            )

            if (uiState.hasSearched && uiState.searchResults.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Keine Spieler gefunden.", color = QuizlyMuted, textAlign = TextAlign.Center)
                }
            } else if (uiState.searchResults.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 12.dp)) {
                    items(uiState.searchResults) { user ->
                        FriendRow(user = user, onInvite = { viewModel.openInvite(user) })
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.size(70.dp).background(Color(0x22FFFFFF), CircleShape))
                    Text(
                        "Lade deine Freunde zu einem Duell ein!",
                        color = QuizlyWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                    Text("(Demo) Hier später echte Freundesliste.", color = QuizlyMuted, textAlign = TextAlign.Center)
                }
            }
        }

        if (uiState.showInviteOverlay) {
            Box(modifier = Modifier.fillMaxSize().background(Color(0xAA000000)), contentAlignment = Alignment.Center) {
                Column(
                    modifier = Modifier
                        .width(320.dp)
                        .background(Color(0xEFFFFFFF), RoundedCornerShape(22.dp))
                        .padding(18.dp)
                ) {
                    Text(
                        "Freunde einladen",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF1E2A6A),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        uiState.inviteStatusMessage ?: "(Demo) Später z.B. Code/Link anzeigen.",
                        color = Color(0xFF4B5563),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    )
                    PrimaryButton(text = "Einladen", backgroundColor = QuizlyBlueBtn, onClick = viewModel::sendInvite)
                    PrimaryButton(text = "Schließen", backgroundColor = QuizlyDarkBtn, onClick = viewModel::closeInvite, modifier = Modifier.padding(top = 8.dp))
                }
            }
        }
    }
}

@Composable
private fun LeaderboardSection(entries: List<com.quizly.android.data.model.LeaderboardEntry>, unavailable: Boolean) {
    if (!unavailable && entries.isEmpty()) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .background(QuizlyCardStrong, RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Text("🏆 Bestenliste", color = QuizlyWhite, fontWeight = FontWeight.Black, modifier = Modifier.padding(bottom = 8.dp))
        if (unavailable) {
            Text("Bestenliste derzeit nicht verfügbar", color = QuizlyMuted)
        } else {
            entries.forEachIndexed { index, entry ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Text("${entry.rank ?: index + 1}.", color = QuizlyMuted, modifier = Modifier.width(28.dp))
                    Text(entry.nickname ?: "?", color = QuizlyWhite, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    Text("${entry.score ?: 0}", color = QuizlyWhite, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun FriendRow(user: Spieler, onInvite: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(QuizlyCardStrong, RoundedCornerShape(18.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(40.dp).background(QuizlyCyan, CircleShape))
        Text(user.nickname, color = QuizlyWhite, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 12.dp).weight(1f))
        androidx.compose.material3.TextButton(onClick = onInvite) {
            Text("Einladen", color = QuizlyCyan, fontWeight = FontWeight.Bold)
        }
    }
}
