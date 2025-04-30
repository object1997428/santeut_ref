package com.santeut.ui.navigation.top

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.santeut.ui.chat.ChatListScreen
import com.santeut.ui.chat.ChatScreen
import com.santeut.ui.noti.NotiScreen

fun NavGraphBuilder.TopNavGraph(
    navController: NavController
) {
    navigation(
        startDestination = "chatList",
        route = "chat_graph"
    ) {
        composable("chatList") {
            ChatListScreen(navController)
        }

        composable(
            route = "chatRoom/{partyId}/{partyName}",
            arguments = listOf(
                navArgument("partyId") { type = NavType.IntType },
                navArgument("partyName") { type = NavType.StringType }
            )
        ) {backStackEntry->
            val partyId = backStackEntry.arguments?.getInt("partyId")?:0
            val partyName = backStackEntry.arguments?.getString("partyName")?:"소모임"
            ChatScreen(partyId)
        }
        composable(
            route="noti"
        ){
            NotiScreen(navController)
        }
    }
}