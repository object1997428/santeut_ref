package com.santeut.ui.guild

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.santeut.ui.community.party.JoinPartyScreen
import com.santeut.ui.navigation.top.GuildTopBar
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun GuildScreen(
    guildId: Int,
    navController: NavController,
    guildViewModel: GuildViewModel = hiltViewModel()
) {
    val pages = listOf("정보", "게시판", "소모임", "랭킹")
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    val guild by guildViewModel.guild.observeAsState()

    LaunchedEffect(key1 = guildId) {
        guildViewModel.getGuild(guildId)
    }

    Scaffold {
        Column(modifier = Modifier.fillMaxWidth()) {
            guild?.let { it1 -> GuildTopBar(navController, it1) }
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
                    0 -> GuildInfoScreen(guild)
                    1 -> GuildCommunityScreen(guildId, navController)
                    2 -> JoinPartyScreen(guildId)
                    3 -> GuildRankingScreen(guildId)
                    else -> Text("Unknown page")
                }
            }
        }
    }

}

