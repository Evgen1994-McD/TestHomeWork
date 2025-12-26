package com.example.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.core.ui.history.HistoryScreen
import com.example.core.ui.translation.TranslationScreen

sealed class Screen(val route: String) {
    object Translation : Screen("translation")
    object History : Screen("history")
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Translation.route
    ) {
        composable(Screen.Translation.route) {
            TranslationScreen()
        }
        composable(Screen.History.route) {
            HistoryScreen()
        }
    }
}

