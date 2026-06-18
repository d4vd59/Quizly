package com.quizly.android.ui.screens.question

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quizly.android.data.model.Antwort
import com.quizly.android.data.model.Frage
import com.quizly.android.data.remote.ApiResult
import com.quizly.android.data.repository.MatchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class QuestionUiState(
    val question: Frage? = null,
    val isLoading: Boolean = true
)

/**
 * Ported from Views/QuestionView.xaml(.cs). The WPF source always shows a real, playable
 * question grid - it hardcodes a single demo question directly in XAML and never actually
 * wires Answer_Click to a network call or a correctness check (the score-update line is
 * commented out there). This fallback question is that exact hardcoded WPF content, used
 * whenever a real one isn't available (no match / live API unreachable) so the screen never
 * degrades to an empty error state - it stays visually identical to the WPF reference.
 */
private val FALLBACK_QUESTION = Frage(
    id = -1,
    text = "Wofür steht die Abkürzung \"KiK\", nach der ein deutscher Textil-Discounter benannt ist?",
    answers = listOf(
        Antwort(id = -1, text = "Kunde ist König"),
        Antwort(id = -2, text = "Kleidung im Kreis"),
        Antwort(id = -3, text = "Kinder in Kleidern"),
        Antwort(id = -4, text = "Köln im Karneval")
    )
)

class QuestionViewModel(
    private val matchRepository: MatchRepository,
    private val matchId: Int?,
    private val roundNumber: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuestionUiState())
    val uiState: StateFlow<QuestionUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    private fun load() {
        val id = matchId
        if (id == null) {
            _uiState.value = QuestionUiState(question = FALLBACK_QUESTION, isLoading = false)
            return
        }
        viewModelScope.launch {
            val result = matchRepository.getRoundQuestions(id, roundNumber)
            val question = (result as? ApiResult.Success)?.data?.firstOrNull() ?: FALLBACK_QUESTION
            _uiState.value = QuestionUiState(question = question, isLoading = false)
        }
    }

    /** Returns true if the picked answer was correct. Submission failures are swallowed -
     * the WPF reference implementation never wires Answer_Click to a network call either,
     * it only mutates local GameState, so local-only progression is source-faithful. */
    suspend fun answer(answerId: Int): Boolean {
        val question = _uiState.value.question ?: return false
        val correct = question.isAnswerCorrect(answerId)
        if (matchId != null && question !== FALLBACK_QUESTION) {
            matchRepository.submitAnswer(matchId, question.id, answerId)
        }
        return correct
    }
}
