package com.santeut.ui.navigation.bottom

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.santeut.ui.guild.CreateGuildPostScreen
import com.santeut.ui.guild.CreateGuildScreen
import com.santeut.ui.guild.GuildApplyListScreen
import com.santeut.ui.guild.GuildCommunityScreen
import com.santeut.ui.guild.GuildMemberListScreen
import com.santeut.ui.guild.GuildPostDetailScreen
import com.santeut.ui.guild.GuildScreen
import com.santeut.ui.guild.MyGuildScreen
import com.santeut.ui.guild.UpdateGuildScreen
import com.santeut.ui.map.MapViewModel
import com.santeut.ui.party.InputPartyInfoScreen
import com.santeut.ui.party.SelectedMountain


fun NavGraphBuilder.GuildNavGraph(
    navController: NavHostController,
    mapViewModel: MapViewModel
) {
    navigation(
        startDestination = "guild",
        route = "guild_graph"
    ) {

        // 나의 모임 페이지
        composable("guild") {
            MyGuildScreen(navController = navController, mapViewModel = mapViewModel)
        }

        // 동호회 만들기
        composable("createGuild") {
            CreateGuildScreen(navController)
        }

        // 동호회 수정하기 (동호회 관리)
        composable(
            route = "updateGuild/{guildId}",
            arguments = listOf(
                navArgument("guildId") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val guildId = backStackEntry.arguments?.getInt("guildId") ?: 0
            UpdateGuildScreen(navController, guildId)
        }

        // 동호회 상세 페이지
        composable(
            route = "getGuild/{guildId}",
            arguments = listOf(
                navArgument("guildId") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val guildId = backStackEntry.arguments?.getInt("guildId") ?: 0
            GuildScreen(guildId, navController)
        }

        // 동호회 게시판
        composable(
            route = "guildCommunity/{guildId}",
            arguments = listOf(navArgument("guildId") { type = NavType.IntType })
        ) { backStackEntry ->
            val guildId = backStackEntry.arguments?.getInt("guildId") ?: 0
            GuildCommunityScreen(guildId, navController)
        }

        // 동호회 게시판 글쓰기
        composable(
            route = "createGuildPost/{guildId}",
            arguments = listOf(
                navArgument("guildId") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val guildId = backStackEntry.arguments?.getInt("guildId") ?: 0
            CreateGuildPostScreen(guildId, navController)
        }

        // 동호회 게시글 상세조회
        composable(
            route = "getGuildPost/{guildPostId}",
            arguments = listOf(
                navArgument("guildPostId") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val guildPostId = backStackEntry.arguments?.getInt("guildPostId") ?: 0
            GuildPostDetailScreen(guildPostId, navController)
        }

        // 동호회 회원 조회
        composable(
            route = "guildMemberList/{guildId}",
            arguments = listOf(
                navArgument("guildId") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val guildId = backStackEntry.arguments?.getInt("guildId") ?: 0
            GuildMemberListScreen(guildId, navController)
        }

        // 동호회 가입 신청 목록 조회
        composable(
            route = "guildApplyList/{guildId}",
            arguments = listOf(
                navArgument("guildId") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val guildId = backStackEntry.arguments?.getInt("guildId") ?: 0
            GuildApplyListScreen(guildId, navController)
        }

        // 소모임 생성 시 산 선택하기
        composable("createParty") {
            SelectedMountain(null, navController)
        }

        // 소모임 생성 시 산 선택하기
        composable(
            route = "createParty/{guildId}",
            arguments = listOf(navArgument("guildId") { type = NavType.IntType })
        ) { backStackEntry ->
            val guildId = backStackEntry.arguments?.getInt("guildId") ?: 0
            SelectedMountain(guildId, navController)
        }

        // 동호회가 있는 소모임 생성
        composable(
            route = "createParty/{guildId}/{mountainId}/{courseIds}",
            arguments = listOf(
                navArgument("guildId") { type = NavType.IntType },
                navArgument("mountainId") { type = NavType.IntType },
                navArgument("courseIds") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val guildId = backStackEntry.arguments?.getInt("guildId") ?: 0
            val mountainId = backStackEntry.arguments?.getInt("mountainId") ?: 0
            val courseIdsString = backStackEntry.arguments?.getString("courseIds") ?: ""
            val selectedCourseIds = courseIdsString.split(",").map { it.toIntOrNull() ?: 0 }
            InputPartyInfoScreen(guildId, mountainId, selectedCourseIds, navController)
        }

        // 동호회가 없는 소모임 생성
        composable(
            route = "createParty/{mountainId}/{courseIds}",
            arguments = listOf(
                navArgument("mountainId") { type = NavType.IntType },
                navArgument("courseIds") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            val mountainId = backStackEntry.arguments?.getInt("mountainId") ?: 0
            val courseIdsString = backStackEntry.arguments?.getString("courseIds") ?: ""
            val selectedCourseIds = courseIdsString.split(",").map { it.toIntOrNull() ?: 0 }
            InputPartyInfoScreen(null, mountainId, selectedCourseIds, navController)
        }

    }
}