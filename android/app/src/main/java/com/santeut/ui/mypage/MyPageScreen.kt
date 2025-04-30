package com.santeut.ui.mypage

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun MyPageScreen(
    navController: NavController
) {
    val pages = listOf("프로필", "산행 기록", "등산 일정")
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold {
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
                            androidx.compose.material.Text(
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
                    0 -> MyProfileScreen()
                    1 -> MyHikingScreen()
                    2 -> MyScheduleScreen()
                    else -> Text("Unknown page")
                }
            }
        }
    }
}
