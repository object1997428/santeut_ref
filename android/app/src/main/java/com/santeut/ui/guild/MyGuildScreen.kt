package com.santeut.ui.guild

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.santeut.ui.map.MapViewModel
import com.santeut.ui.party.MyPartyListScreen
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MyGuildScreen(
    navController: NavController,
    mapViewModel: MapViewModel
) {
    val pages = listOf("동호회", "소모임")
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showBottomSheet = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        },
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                backgroundColor = Color.White,
                divider = {},
                indicator = { tabPositions ->
                    Canvas(
                        modifier = Modifier
                            .pagerTabIndicatorOffset(pagerState, tabPositions)
                            .fillMaxWidth()
                            .height(2.dp)
                    ) {
                        drawRoundRect(
                            color = Color(0xff678C40),
                            cornerRadius = CornerRadius(x = 10.dp.toPx(), y = 10.dp.toPx())
                        )
                    }
                }
            ) {
                pages.forEachIndexed { index, title ->
                    Tab(
                        text = {
                            Text(
                                text = title,
                                color = if (pagerState.currentPage == index) Color(0xff678C40) else Color(
                                    0xff666E7A
                                ),
                            )
                        },
                        selected = pagerState.currentPage == index,

                        onClick = {
                            coroutineScope.launch {
                                pagerState.scrollToPage(index)
                            }
                        }
                    )
                }
            }

            HorizontalPager(
                count = pages.size,
                state = pagerState
            ) { page ->
                when (page) {
                    0 -> MyGuildListScreen(navController)
                    1 -> MyPartyListScreen(
                        navController = navController,
                        mapViewModel = mapViewModel
                    )

                    else -> Text("Unknown page")
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState
            ) {
                Surface(modifier = Modifier.padding(16.dp)) {
                    Column {
                        TextButton(onClick = {
                            // 동호회 생성 로직
                            navController.navigate("createGuild")
                            coroutineScope.launch { sheetState.hide() }
                        }) {
                            Text("동호회 만들기", style = MaterialTheme.typography.titleMedium)
                        }
                        TextButton(onClick = {
                            // 소모임 생성 로직
                            navController.navigate("createParty")
                            coroutineScope.launch { sheetState.hide() }
                        }) {
                            Text("소모임 만들기", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}