package com.quizly.android.di

import android.content.Context
import com.quizly.android.data.local.SessionManager
import com.quizly.android.data.local.TokenStore
import com.quizly.android.data.remote.ApiService
import com.quizly.android.data.remote.NetworkModule
import com.quizly.android.data.repository.AuthRepository
import com.quizly.android.data.repository.CatalogRepository
import com.quizly.android.data.repository.MatchRepository
import com.quizly.android.data.repository.SocialRepository
import com.quizly.android.data.repository.UserRepository

/** Manual DI singleton container, attached in QuizlyApplication. */
class AppContainer(context: Context) {

    val sessionManager: SessionManager by lazy { SessionManager() }
    val tokenStore: TokenStore by lazy { TokenStore(context) }
    val apiService: ApiService by lazy { NetworkModule.createApiService(sessionManager) }

    val authRepository: AuthRepository by lazy { AuthRepository(apiService, tokenStore, sessionManager) }
    val userRepository: UserRepository by lazy { UserRepository(apiService) }
    val catalogRepository: CatalogRepository by lazy { CatalogRepository(apiService) }
    val matchRepository: MatchRepository by lazy { MatchRepository(apiService) }
    val socialRepository: SocialRepository by lazy { SocialRepository(apiService) }
}
