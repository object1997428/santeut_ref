package com.santeut.ui.guild

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.santeut.R
import com.santeut.data.model.response.GuildResponse
import com.santeut.designsystem.theme.DarkGreen

@Composable
fun GuildInfoScreen(guild: GuildResponse?) {
    Column(
        modifier = Modifier
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Top
    ) {

        if (guild != null) {

            AsyncImage(
                model = guild.guildProfile ?: R.drawable.logo,
                contentDescription = "동호회 사진",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .fillMaxHeight(.3f)
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {

                Spacer(modifier = Modifier.height(8.dp))

                // 동호회 이름
                Text(
                    text = guild.guildName ?: "",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // 동호회 설명
                Text(
                    text = guild.guildInfo ?: "",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // 동호회 인원, 성별, 연령
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "인원 ",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Text(
                            text = "${guild.guildMember}명",
                            color = DarkGreen,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "성별 ",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Text(
                            text = "${genderToString(guild)}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "연령 ",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Text(
                            text = "${guild.guildMinAge}세 ~ ${guild.guildMaxAge}세",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkGreen,
                        )
                    }
                }
            }
        }
    }
}

fun genderToString(guild: GuildResponse?): String {
    return when (guild?.guildGender) {
        'F' -> "여"
        'M' -> "남"
        else -> "성별 무관"
    }
}

fun regionName(regionId: Int?): String {
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
        else -> "지역 무관"
    }
}
