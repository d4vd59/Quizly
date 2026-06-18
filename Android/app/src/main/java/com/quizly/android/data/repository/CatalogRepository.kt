package com.quizly.android.data.repository

import com.quizly.android.data.model.Kategorie
import com.quizly.android.data.model.Schwierigkeitsgrad
import com.quizly.android.data.model.Settings
import com.quizly.android.data.remote.ApiResult
import com.quizly.android.data.remote.ApiService
import com.quizly.android.data.remote.dto.GameModeInfo
import com.quizly.android.data.remote.dto.GameStateInfo

/**
 * Ported from api_client.py get_categories/get_category/get_difficulties/get_difficulty/
 * get_settings/get_game_modes/get_game_states. Read-mostly reference data.
 */
class CatalogRepository(private val api: ApiService) {

    suspend fun getCategories(number: Int? = null): ApiResult<List<Kategorie>> =
        safeCall { api.getCategories(number) }

    suspend fun getCategory(id: Int): ApiResult<Kategorie> = safeCall { api.getCategory(id) }

    suspend fun getDifficulties(): ApiResult<List<Schwierigkeitsgrad>> = safeCall { api.getDifficulties() }

    suspend fun getDifficulty(id: Int): ApiResult<Schwierigkeitsgrad> = safeCall { api.getDifficulty(id) }

    suspend fun getSettings(): ApiResult<Settings> = safeCall { api.getSettings() }

    suspend fun getGameModes(): ApiResult<List<GameModeInfo>> = safeCall { api.getGameModes() }

    suspend fun getGameStates(): ApiResult<List<GameStateInfo>> = safeCall { api.getGameStates() }
}
