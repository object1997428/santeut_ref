package com.santeut.ui.noti

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.EmojiSupportMatch
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.santeut.data.model.response.NotificationResponse
import com.santeut.ui.community.CommonViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NotiScreen(
    navController: NavController,
    commonViewModel: CommonViewModel = hiltViewModel()
) {

    val notiList by commonViewModel.notiList.observeAsState(emptyList())

    LaunchedEffect(key1 = null) {
        commonViewModel.getNotificationList()
    }


    Scaffold {
        Column {
            Box(
                modifier = Modifier
                    .height(45.dp)
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .background(Color(0xFFEFEFF0)),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(text = "모두 읽기",
                    color = Color.Black,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .clickable {
                            /*
                            *  누르면 알림 모두 삭제 되도록 구현
                            * */
                        }
                )
            }
            if (notiList.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.Start
                ) {
                    items(notiList) { noti ->
                        NotiMessage(noti, navController)
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.material3.Text(
                        text = "알람이 없습니다.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun NotiMessage(noti: NotificationResponse, navController: NavController) {
    // 이모지
    // 1. 게시글에대한 댓글(C)
    // 2. 게시글에대한 좋아요(L)
    // 3. 동호회 승인(G)
    // 4. 소모임장이 등산 시작을 누름(P)
    // 5. 등산(H)
    val emoji = when (noti.referenceType) {
        "TC", "GC", "CC" -> "💬"
        "TL", "GL", "CL" -> "💗"
        "GR" -> "✔"
        "P" -> "⛰"
        "HS" -> "⛰"
        else -> "▪"
    }
    Row(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .clickable {
                    // 팁 게시판 좋아요이면
                    if (noti.referenceType[0] == 'T')
                        navController.navigate("readPost/${noti.referenceId}/${noti.referenceType}")
                    else if (noti.referenceType[0] == 'G' && noti.referenceType[1] != 'R')
                        navController.navigate("getGuildPost/${noti.referenceId}")
                    else if (noti.referenceType == "GR")
                        navController.navigate("getGuild/${noti.referenceId}")
                }
                .padding(horizontal = 40.dp, vertical = 10.dp),
        ) {
            Text(text = emoji)
            Spacer(modifier = Modifier.width(5.dp))
            Column(
            ) {
                Text(
                    text = noti.alarmTitle,
                    color = Color(0xFF76797D),
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            emojiSupportMatch = EmojiSupportMatch.None
                        )
                    )
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = noti.alarmContent)
            }
        }
    }
}