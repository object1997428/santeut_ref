package com.santeut

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.santeut.ui.map.MapViewModel
import com.santeut.ui.navigation.SanteutNavGraph
import com.santeut.ui.navigation.bottom.BottomNavBar
import com.santeut.ui.navigation.top.TopBar
import com.santeut.ui.wearable.WearableViewModel

@Composable
fun SanteutApp(
    wearableViewModel: WearableViewModel,
    guildId: Int,
    onClearData: () -> Unit
) {
    val navController = rememberNavController()
    val mapViewModel = hiltViewModel<MapViewModel>()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight(),
        topBar = {
            TopBar(
                navController,
                currentTap = navController.currentBackStackEntryAsState().value?.destination?.route
            )
        },
        bottomBar = {
            BottomNavBar(
                currentTap = navController.currentBackStackEntryAsState().value?.destination?.route,
                onTabClick = {
                    navController.navigate(it.route)
                }
            )
        },
        content = { padding ->
            Box(
                modifier = Modifier.padding(padding)
            ) {
                SanteutNavGraph(
                    navController = navController,
                    wearableViewModel = wearableViewModel,
                    mapViewModel = mapViewModel,
                    guildId = guildId,
                    onClearData = onClearData
                )
            }
        }
    )
}
