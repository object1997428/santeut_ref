package com.santeut.ui.mypage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Hiking
import androidx.compose.material.icons.outlined.Healing
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.santeut.R
import com.santeut.data.model.response.MyProfileResponse

@Composable
fun MyProfileScreen(userViewModel: UserViewModel = hiltViewModel()) {
    val myProfile by userViewModel.myProfile.observeAsState()

    LaunchedEffect(Unit) {
        userViewModel.getMyProfile()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        ProfileHeader(myProfile)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "나의 등산 기록",
            style = MaterialTheme.typography.h5,
        )
        Spacer(modifier = Modifier.height(8.dp))
        ProfileStats(myProfile)
    }
}

@Composable
fun ProfileHeader(myProfile: MyProfileResponse?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = 5.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            ) {
                AsyncImage(
                    model = myProfile?.userProfile ?: R.drawable.logo,
                    contentDescription = "프로필 사진",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${myProfile?.userNickname}",
                style = MaterialTheme.typography.h5,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Lv. ${myProfile?.userTierName}",
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(40.dp))
                Text(
                    text = "포인트 ${myProfile?.userTierPoint}P",
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun ProfileStats(myProfile: MyProfileResponse?) {
    StatCard(
        icon = Icons.AutoMirrored.Filled.DirectionsWalk,
        contentDescription = "걸음",
        statText = buildAnnotatedString {
            append("총 ")
            withStyle(style = SpanStyle(color = Color.White)) {
                append("${myProfile?.userDistance ?: 0}m")
            }
            append(" 걸었어요!")
        }
    )
    StatCard(
        icon = Icons.Outlined.Timer,
        contentDescription = "시간",
        statText = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.White)) {
                append("${myProfile?.userMoveTime ?: 0}분")
            }
            append(" 동안 걸었어요!")
        }
    )
    StatCard(
        icon = Icons.Outlined.Healing,
        contentDescription = "등산",
        statText = buildAnnotatedString {
            append("지금까지 등반을 ")
            withStyle(style = SpanStyle(color = Color.White)) {
                append("${myProfile?.userHikingCount ?: 0}번")
            }
            append(" 완료했어요!")
        }
    )
    StatCard(
        icon = Icons.Filled.Hiking,
        contentDescription = "산 정복",
        statText = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.White)) {
                append("${myProfile?.userHikingMountain ?: 0}개의")
            }
            append(" 산을 정복했어요!")
        }
    )
}

@Composable
fun StatCard(icon: ImageVector, contentDescription: String, statText: AnnotatedString) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 5.dp,
        backgroundColor = Color(0xff678C40)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 16.dp)
            )
            Text(
                text = statText,
                style = MaterialTheme.typography.body1
            )
        }
    }
}
