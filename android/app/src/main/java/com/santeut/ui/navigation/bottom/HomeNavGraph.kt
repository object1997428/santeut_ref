package com.santeut.ui.navigation.bottom

import CommunityScreen
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.santeut.ui.home.HomeScreen


fun NavGraphBuilder.HomeNavGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = "home",
        route = "home_graph"
    ) {
        composable("home") {
            HomeScreen(navController)
        }
        composable(
            "community/{initialPage}",
            arguments = listOf(navArgument("initialPage") { type = NavType.IntType })
        ) { backStackEntry ->
            val initialPage = backStackEntry.arguments?.getInt("initialPage") ?: 0
            CommunityScreen(navController, initialPage, guildId = 0, onClearData = {})
        }
    }
}
