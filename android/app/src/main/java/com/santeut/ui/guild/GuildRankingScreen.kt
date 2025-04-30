package com.santeut.ui.guild

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.santeut.R
import com.santeut.data.model.response.RankingResponse
import com.santeut.designsystem.theme.CustomGray
import com.santeut.designsystem.theme.Green
import com.santeut.designsystem.theme.customTypography

@Composable
fun GuildRankingScreen(
    guildId: Int,
    guildViewModel: GuildViewModel = hiltViewModel()
) {
    val rankingList by guildViewModel.rankingList.observeAsState()
    val coroutineScope = rememberCoroutineScope()

    var showHeight by remember { mutableStateOf(true) }
    var showDistance by remember { mutableStateOf(false) }
    var showCount by remember { mutableStateOf(false) }

    val activeColor = Green
    val inactiveColor = CustomGray

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    showHeight = true
                    showDistance = false
                    showCount = false
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = White,
                    backgroundColor = if (showHeight) activeColor else inactiveColor,
                    disabledContentColor = CustomGray
                ),
                shape = RoundedCornerShape(50.dp)
            ) {
                Text("최고고도")
            }
            Button(
                onClick = {
                    showHeight = false
                    showDistance = true
                    showCount = false
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = White,
                    backgroundColor = if (showDistance) activeColor else inactiveColor,
                    disabledContentColor = CustomGray
                ),
                shape = RoundedCornerShape(50.dp)
            ) {
                Text("최장거리")
            }
            Button(
                onClick = {
                    showHeight = false
                    showDistance = false
                    showCount = true
                }, colors = ButtonDefaults.buttonColors(
                    contentColor = White,
                    backgroundColor = if (showCount) activeColor else inactiveColor,
                    disabledContentColor = CustomGray
                ),
                shape = RoundedCornerShape(50.dp)
            ) {
                Text("최다등반")
            }
        }
        if (showHeight) {
            guildViewModel.getRanking(guildId, 'H') // 최고고도
        } else if (showDistance) {
            guildViewModel.getRanking(guildId, 'D') // 최장거리
        } else if (showCount) {
            guildViewModel.getRanking(guildId, 'C') // 최다등반
        }

        Spacer(modifier = Modifier.height(16.dp))

        rankingList?.let { rankingList ->
            if (rankingList.isEmpty()) {
                Text(
                    text = "랭킹이 존재하지 않습니다.",
                    textAlign = TextAlign.Center
                )
            } else {
                rankingList.forEach { rank ->
                    RankingItem(rank)
                }
            }
        } ?: run {
            Text("Loading...")
        }
    }
}

@Composable
fun RankingItem(rank: RankingResponse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(8.dp)
            .shadow(spotColor = LightGray, elevation = 10.dp)

            .clip(RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(8.dp),
        elevation = 10.dp,
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${rank.order}",
                modifier = Modifier
                    .padding(vertical = 0.dp, horizontal = 15.dp),
                style = customTypography.titleLarge
            )
            AsyncImage(
                model = rank.userProfile ?: R.drawable.logo,
                contentDescription = "프로필 사진",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(2.dp, Green, CircleShape)
            )

            Text(
                text = rank.userNickname,
                modifier =
                Modifier
                    .padding(horizontal = 15.dp, vertical = 0.dp),
                style = customTypography.bodyLarge
            )
            Text(
                text = rank.score,
                textAlign = TextAlign.Right,
                modifier = Modifier
                    .weight(2f),
                style = customTypography.bodyLarge
            )
        }

    }

}
