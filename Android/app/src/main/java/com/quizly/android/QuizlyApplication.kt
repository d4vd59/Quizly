package com.quizly.android

import android.app.Application
import com.quizly.android.di.AppContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class QuizlyApplication : Application() {

    lateinit var container: AppContainer
    private val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        // Hydrate the in-memory session from the persisted token so AuthInterceptor can
        // read it synchronously - mirrors api_client.py's _load_token() at construction time.
        applicationScope.launch {
            container.authRepository.restoreSession()
        }
    }
}
