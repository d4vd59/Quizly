package com.quizly.android.ui.screens.categorypick

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quizly.android.data.local.SessionManager
import com.quizly.android.data.remote.ApiResult
import com.quizly.android.data.repository.CatalogRepository
import com.quizly.android.data.repository.MatchRepository
import com.quizly.android.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CategoryUi(val id: Int?, val name: String, val description: String, val emoji: String)

data class CategoryPickUiState(
    val categories: List<CategoryUi> = emptyList(),
    val isPlayerTurn: Boolean = true,
    val isLoading: Boolean = true
)

/** Ported from Views/CategoryPickView.xaml.cs - keeps the hardcoded 8-category/emoji
 * fallback list as a seed when the live API returns too few categories. */
class CategoryPickViewModel(
    private val catalogRepository: CatalogRepository,
    private val matchRepository: MatchRepository,
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager,
    private val isSinglePlayer: Boolean,
    private val existingMatchId: Int?,
    private val onMatchCreated: (Int) -> Unit
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryPickUiState())
    val uiState: StateFlow<CategoryPickUiState> = _uiState.asStateFlow()

    private val fallbackCategories = listOf(
        CategoryUi(null, "Macht & Geld", "Politik und Wirtschaft", "💰"),
        CategoryUi(null, "Musik", "Songs und Künstler", "🎵"),
        CategoryUi(null, "Gaming", "Videospiele", "🕹"),
        CategoryUi(null, "Geschichte", "Vergangene Ereignisse", "📜"),
        CategoryUi(null, "Sport", "Sportarten und Events", "⚽"),
        CategoryUi(null, "Filme & Serien", "Unterhaltung", "🎬"),
        CategoryUi(null, "Wissenschaft", "Forschung und Technik", "🔬"),
        CategoryUi(null, "Geografie", "Länder und Städte", "🌍")
    )

    init {
        viewModelScope.launch {
            ensureMatch()
            loadCategories()
        }
    }

    private suspend fun ensureMatch() {
        if (!isSinglePlayer || existingMatchId != null) return
        val identifier = sessionManager.currentUser.value?.userIdentifier ?: return

        val userResult = userRepository.getUser(identifier)
        val userId = (userResult as? ApiResult.Success)?.data?.userId ?: return

        val matchResult = matchRepository.createSingleMatch(userId, difficulty = 1)
        (matchResult as? ApiResult.Success)?.data?.matchId?.let(onMatchCreated)
    }

    private suspend fun loadCategories() {
        val result = catalogRepository.getCategories(number = 3)
        val apiCategories = (result as? ApiResult.Success)?.data.orEmpty().map { cat ->
            val fallbackMatch = fallbackCategories.find { it.name.equals(cat.name, ignoreCase = true) }
            CategoryUi(
                id = cat.id,
                name = cat.name,
                description = fallbackMatch?.description ?: "Quizfragen",
                emoji = fallbackMatch?.emoji ?: "❓"
            )
        }

        val finalCategories = if (apiCategories.size >= 3) apiCategories else fallbackCategories.shuffled().take(3)
        _uiState.value = _uiState.value.copy(categories = finalCategories, isLoading = false)
    }
}
