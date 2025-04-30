package com.santeut.ui.navigation.bottom

import CommunityScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.santeut.ui.community.CommonViewModel
import com.santeut.ui.community.PostViewModel
import com.santeut.ui.community.common.ReadPostScreen
import com.santeut.ui.community.course.CreateCoursePostScreen
import com.santeut.ui.community.tips.CreateTipPostScreen
import com.santeut.ui.community.tips.PostTipsScreen

fun NavGraphBuilder.CommunityNavGraph(
    navController: NavHostController,
    guildId: Int,
    onClearData: () -> Unit
) {
    navigation(
        startDestination = "community",
        route = "community_graph"
    ) {
        composable("community") {
            CommunityScreen(navController, guildId = guildId, onClearData = onClearData)
        }
        composable("postTips") {
            PostTipsScreen(navController)
        }
        composable(
            route = "createPost/{postType}",
            arguments = listOf(navArgument("postType") { type = NavType.StringType })
        ) { backStackEntry ->
            val postType = backStackEntry.arguments?.getString("postType") ?: "T"
            val postViewModel = hiltViewModel<PostViewModel>()
            if (postType == "T")
                CreateTipPostScreen(navController, postViewModel, postType.first())
            else
                CreateCoursePostScreen(navController, postViewModel, postType.first())
        }
        composable(
            route = "readPost/{postId}/{postType}",
            arguments = listOf(
                navArgument("postId") { type = NavType.IntType },
                navArgument("postType") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getInt("postId") ?: 0
            val postType = backStackEntry.arguments?.getString("postType") ?: "T"
            val postViewModel = hiltViewModel<PostViewModel>()
            val commonViewModel = hiltViewModel<CommonViewModel>()
            ReadPostScreen(postId, postType.first(), postViewModel, commonViewModel, navController)
        }
    }
}
