package com.quizly.android.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.quizly.android.di.AppContainer
import com.quizly.android.ui.screens.categorypick.CategoryPickScreen
import com.quizly.android.ui.screens.duel.DuelOverviewScreen
import com.quizly.android.ui.screens.friends.FriendsScreen
import com.quizly.android.ui.screens.gamemode.GameModeSelectionScreen
import com.quizly.android.ui.screens.home.HomeScreen
import com.quizly.android.ui.screens.login.LoginScreen
import com.quizly.android.ui.screens.matchmaking.MatchmakingScreen
import com.quizly.android.ui.screens.question.QuestionScreen
import com.quizly.android.ui.screens.register.RegisterScreen
import com.quizly.android.ui.screens.result.ResultScreen
import com.quizly.android.ui.screens.singleplayer.SinglePlayerScreen
import com.quizly.android.ui.state.GameSessionViewModel

@Composable
fun QuizlyApp(appContainer: AppContainer) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.hierarchy?.firstOrNull()?.route
    val gameSession: GameSessionViewModel = viewModel()

    Scaffold(
        containerColor = androidx.compose.ui.graphics.Color.Transparent,
        bottomBar = {
            if (currentRoute in Routes.BOTTOM_BAR_ROUTES) {
                BottomNavBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.LOGIN,
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            composable(Routes.LOGIN) {
                LoginScreen(
                    authRepository = appContainer.authRepository,
                    onLoginSuccess = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = { navController.navigate(Routes.REGISTER) },
                    onGuest = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                )
            }
            composable(Routes.REGISTER) {
                RegisterScreen(
                    authRepository = appContainer.authRepository,
                    onRegisterSuccess = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() },
                    onGoLogin = { navController.popBackStack() }
                )
            }
            composable(Routes.HOME) {
                HomeScreen(
                    userRepository = appContainer.userRepository,
                    matchRepository = appContainer.matchRepository,
                    authRepository = appContainer.authRepository,
                    sessionManager = appContainer.sessionManager,
                    onNewGame = { navController.navigate(Routes.GAME_MODE) },
                    onPlayMatch = { match ->
                        gameSession.setMatchId(match.matchId)
                        navController.navigate(Routes.DUEL_OVERVIEW)
                    },
                    onViewResult = { match ->
                        gameSession.setMatchId(match.matchId)
                        navController.navigate(Routes.RESULT)
                    },
                    onLoggedOut = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.HOME) { inclusive = true }
                        }
                    }
                )
            }
            composable(Routes.FRIENDS) {
                FriendsScreen(
                    userRepository = appContainer.userRepository,
                    socialRepository = appContainer.socialRepository,
                    sessionManager = appContainer.sessionManager
                )
            }
            composable(Routes.GAME_MODE) {
                GameModeSelectionScreen(
                    onBack = { navController.popBackStack() },
                    onSingleplayer = {
                        gameSession.startSinglePlayer()
                        navController.navigate(Routes.SINGLE_PLAYER) {
                            popUpTo(Routes.GAME_MODE) { inclusive = true }
                        }
                    },
                    onMultiplayer = { navController.navigate(Routes.MATCHMAKING) }
                )
            }
            composable(Routes.SINGLE_PLAYER) {
                val gameState by gameSession.state.collectAsState()
                SinglePlayerScreen(
                    gameState = gameState,
                    onBack = {
                        navController.navigate(Routes.HOME) { popUpTo(Routes.HOME) { inclusive = true } }
                    },
                    onPlay = { navController.navigate(Routes.categoryPick(true)) }
                )
            }
            composable(Routes.MATCHMAKING) {
                MatchmakingScreen(
                    userRepository = appContainer.userRepository,
                    matchRepository = appContainer.matchRepository,
                    sessionManager = appContainer.sessionManager,
                    onBack = { navController.navigate(Routes.HOME) { popUpTo(Routes.HOME) { inclusive = true } } },
                    onOpponentFound = { opponentName, matchId ->
                        gameSession.startMultiplayer(opponentName)
                        matchId?.let(gameSession::setMatchId)
                        navController.navigate(Routes.DUEL_OVERVIEW) {
                            popUpTo(Routes.MATCHMAKING) { inclusive = true }
                        }
                    }
                )
            }
            composable(Routes.DUEL_OVERVIEW) {
                val gameState by gameSession.state.collectAsState()
                DuelOverviewScreen(
                    gameState = gameState,
                    onBack = { navController.navigate(Routes.HOME) { popUpTo(Routes.HOME) { inclusive = true } } },
                    onPlay = { navController.navigate(Routes.categoryPick(false)) }
                )
            }
            composable(Routes.RESULT) {
                val gameState by gameSession.state.collectAsState()
                ResultScreen(
                    gameState = gameState,
                    onBack = {
                        val target = if (gameState.isSinglePlayer) Routes.HOME else Routes.DUEL_OVERVIEW
                        navController.navigate(target) { popUpTo(Routes.HOME) }
                    },
                    onPlayAgain = {
                        navController.navigate(Routes.categoryPick(gameState.isSinglePlayer)) {
                            popUpTo(Routes.RESULT) { inclusive = true }
                        }
                    }
                )
            }
            composable(
                route = Routes.CATEGORY_PICK_PATTERN,
                arguments = listOf(navArgument("singlePlayer") { type = NavType.BoolType; defaultValue = true })
            ) { backStackEntry ->
                val isSinglePlayer = backStackEntry.arguments?.getBoolean("singlePlayer") ?: true
                val gameState by gameSession.state.collectAsState()
                CategoryPickScreen(
                    catalogRepository = appContainer.catalogRepository,
                    matchRepository = appContainer.matchRepository,
                    userRepository = appContainer.userRepository,
                    sessionManager = appContainer.sessionManager,
                    isSinglePlayer = isSinglePlayer,
                    existingMatchId = gameState.matchId,
                    onMatchCreated = { gameSession.setMatchId(it) },
                    onCategorySelected = { category ->
                        navController.navigate(Routes.question(gameState.currentRound + 1, category.name))
                    }
                )
            }
            composable(
                route = Routes.QUESTION_PATTERN,
                arguments = listOf(
                    navArgument("roundNumber") { type = NavType.IntType },
                    navArgument("categoryName") { type = NavType.StringType; defaultValue = "" }
                )
            ) { backStackEntry ->
                val roundNumber = backStackEntry.arguments?.getInt("roundNumber") ?: 1
                val categoryName = Routes.decodeCategoryName(backStackEntry.arguments?.getString("categoryName"))
                val gameState by gameSession.state.collectAsState()

                QuestionScreen(
                    matchRepository = appContainer.matchRepository,
                    matchId = gameState.matchId,
                    roundNumber = roundNumber,
                    categoryName = categoryName,
                    onAnswered = { correct ->
                        gameSession.advanceRound(playerPoints = if (correct) 1 else 0)
                        if (gameSession.isGameFinished()) {
                            navController.navigate(Routes.RESULT) { popUpTo(Routes.HOME) }
                        } else {
                            val target = if (gameState.isSinglePlayer) Routes.SINGLE_PLAYER else Routes.DUEL_OVERVIEW
                            navController.navigate(target) { popUpTo(target) { inclusive = true } }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun PlaceholderScreen(name: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(name)
    }
}
