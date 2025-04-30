import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.santeut.ui.community.course.PostCourseScreen
import com.santeut.ui.community.guild.JoinGuildScreen
import com.santeut.ui.community.party.JoinPartyScreen
import com.santeut.ui.community.tips.PostTipsScreen
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun CommunityScreen(
    navController: NavController,
    initialPage: Int = 0,
    guildId: Int,
    onClearData: () -> Unit
) {
    val pages = listOf("동호회", "소모임", "등산Tip", "코스공유")
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = initialPage) {
        pagerState.scrollToPage(initialPage)
    }

    Scaffold() {
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
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                                x = 10.dp.toPx(),
                                y = 10.dp.toPx()
                            )
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
                                style = MaterialTheme.typography.titleMedium
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
                    0 -> JoinGuildScreen(guildId = guildId, onClearData = onClearData)
                    1 -> JoinPartyScreen(null)
                    2 -> PostTipsScreen(navController)
                    3 -> PostCourseScreen(navController)
                    else -> Text("Unknown page")
                }
            }
        }
    }
}
