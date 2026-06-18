package com.quizly.android.ui.navigation

import java.net.URLDecoder
import java.net.URLEncoder

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val FRIENDS = "friends"
    const val GAME_MODE = "gameMode"
    const val SINGLE_PLAYER = "singlePlayer"
    const val MATCHMAKING = "matchmaking"
    const val DUEL_OVERVIEW = "duelOverview"
    const val RESULT = "result"

    const val CATEGORY_PICK_PATTERN = "categoryPick?singlePlayer={singlePlayer}"
    const val QUESTION_PATTERN = "question/{roundNumber}?categoryName={categoryName}"

    fun categoryPick(singlePlayer: Boolean) = "categoryPick?singlePlayer=$singlePlayer"
    fun question(roundNumber: Int, categoryName: String) =
        "question/$roundNumber?categoryName=${URLEncoder.encode(categoryName, "UTF-8")}"

    fun decodeCategoryName(encoded: String?): String =
        encoded?.let { URLDecoder.decode(it, "UTF-8") } ?: ""

    /** Routes on which the BottomNavBar (Home/Friends) is visible - mirrors MainWindow.xaml.cs's
     * GoHome()/GoFriends() being the only two call sites that show it. */
    val BOTTOM_BAR_ROUTES = setOf(HOME, FRIENDS)
}
