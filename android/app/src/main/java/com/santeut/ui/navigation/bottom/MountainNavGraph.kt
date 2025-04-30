package com.santeut.ui.navigation.bottom

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.santeut.ui.mountain.MountainListScreen
import com.santeut.ui.mountain.MountainScreen
import com.santeut.ui.party.SelectedCourse

fun NavGraphBuilder.MountainNavGraph(navController: NavController) {
    navigation(
        startDestination = "mountainList/{name}",
        route = "mountain_graph"
    ) {
        composable(
            route = "mountainList/{name}",
            arguments = listOf(
                navArgument("name") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            MountainListScreen(null, null, name, null, navController)
        }
        composable(
            route = "mountainList/{name}/{region}",
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("region") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val region = backStackEntry.arguments?.getString("region")
            MountainListScreen(null, null, name, region, navController)
        }
        composable(route = "mountain/{mountainId}", arrayListOf(
            navArgument("mountainId") { type = NavType.IntType }
        )) { backStackEntry ->
            val mountainId = backStackEntry.arguments?.getInt("mountainId") ?: 0
            MountainScreen(mountainId)
        }


        // 산 선택하기
        composable(
            route = "create/mountainList/{name}",
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""

            MountainListScreen(null, "create", name, null, navController)
        }

        // 코스 선택하기
        composable(
            route = "create/courseList/{mountainId}/{mountainName}",
            arguments = listOf(
                navArgument("mountainId") { type = NavType.IntType },
                navArgument("mountainName") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val mountainId = backStackEntry.arguments?.getInt("mountainId") ?: 0
            val mountainName = backStackEntry.arguments?.getString("mountainName") ?: ""
            SelectedCourse(null, mountainId, mountainName, navController)
        }


        // 산 선택하기
        composable(
            route = "create/mountainList/{name}/{guildId}",
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("guildId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val guildId = backStackEntry.arguments?.getInt("guildId") ?: 0
            MountainListScreen(guildId, "create", name, null, navController)
        }

        // 코스 선택하기
        composable(
            route = "create/courseList/{mountainId}/{mountainName}/{guildId}",
            arguments = listOf(
                navArgument("mountainId") { type = NavType.IntType },
                navArgument("mountainName") { type = NavType.StringType },
                navArgument("guildId") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val mountainId = backStackEntry.arguments?.getInt("mountainId") ?: 0
            val mountainName = backStackEntry.arguments?.getString("mountainName") ?: "'"
            val guildId = backStackEntry.arguments?.getInt("guildId") ?: 0
            SelectedCourse(guildId, mountainId, mountainName, navController)
        }
    }
}