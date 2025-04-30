package com.santeut.ui.guild

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.santeut.R
import com.santeut.data.model.response.GuildResponse

@Composable
fun MyGuildListScreen(
    navController: NavController,
    guildViewModel: GuildViewModel = hiltViewModel()
) {
    val guilds by guildViewModel.guilds.observeAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        guildViewModel.myGuilds()
    }
    if (guilds.isEmpty()) {
        // 동호회 목록이 emptyList()인 경우
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "아직 동호회에 가입하지 않았습니다.",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start
        ) {
            items(guilds) { guild ->
                GuildCard(guild, navController)
            }
        }
    }
}

@Composable
fun GuildCard(guild: GuildResponse, navController: NavController) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(8.dp)
            .clickable {
                navController.navigate("getGuild/${guild.guildId}")
            },
        shape = RoundedCornerShape(8.dp),
        elevation = 8.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = guild.guildProfile ?: R.drawable.logo,
                contentDescription = "동호회 사진",
                modifier = Modifier
                    .size(100.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = guild.guildName,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 5.dp, top = 5.dp)
                )
                {
                    Icon(
                        imageVector = Icons.Filled.PersonOutline,
                        contentDescription = "회원수",
                        modifier = Modifier.size(20.dp),
                        tint = Color(0xff76797D),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${guild.guildMember ?: 0} 명",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xff76797D)
                    )
                }
//                --------------------------
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.PinDrop,
                        contentDescription = "지역",
                        modifier = Modifier.size(20.dp),
                        tint = Color(0xff76797D)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = regionName(guild.regionId),
                        color = Color(0xff76797D),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
//                --------------------------
            }
        }
    }
}

fun regionName(regionId: Int): String {
    return when (regionId) {
        0 -> "전체"
        1 -> "서울"
        2 -> "부산"
        3 -> "대구"
        4 -> "인천"
        5 -> "광주"
        6 -> "대전"
        7 -> "울산"
        8 -> "세종"
        9 -> "경기"
        10 -> "충북"
        11 -> "충남"
        12 -> "전북"
        13 -> "전남"
        14 -> "경북"
        15 -> "경남"
        16 -> "제주"
        17 -> "강원"
        else -> "기타"
    }
}