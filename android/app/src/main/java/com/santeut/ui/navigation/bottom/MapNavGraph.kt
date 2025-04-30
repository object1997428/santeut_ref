package com.santeut.ui.navigation.bottom

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.santeut.ui.map.MapScreen
import com.santeut.ui.map.MapViewModel
import com.santeut.ui.map.SearchPlant
import com.santeut.ui.wearable.WearableViewModel

@ExperimentalNaverMapApi
fun NavGraphBuilder.MapNavGraph(
    navController: NavHostController,
    mapViewModel: MapViewModel,
    wearableViewModel: WearableViewModel
) {
    navigation(
        startDestination = "map",
        route = "map_graph"
    ) {
        composable(
            route = "map"
        ) {
            MapScreen(
                mapViewModel = mapViewModel,
                wearableViewModel = wearableViewModel,
                onNavigateSearchPlant = {
                    navController.navigate(route = "searchPlant")
                }
            )
        }
        composable("searchPlant") {
            SearchPlant()
        }
    }
}
