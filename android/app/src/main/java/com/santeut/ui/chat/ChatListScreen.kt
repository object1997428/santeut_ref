package com.santeut.ui.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.santeut.data.model.response.ChatRoomInfo

@Composable
fun ChatListScreen(
    navController: NavController,
    chatViewModel: ChatViewModel = hiltViewModel()
) {
    val chatrooms by chatViewModel.chatrooms.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        chatViewModel.getChatRoomList()
    }

    Column {
        LazyColumn {
            chatrooms.forEach {
                item { ChatRoom(chatRoomInfo = it, navController = navController) }
            }
        }
    }
}

@Composable
fun ChatRoom(chatRoomInfo: ChatRoomInfo, navController: NavController) {

    Box(
        modifier = Modifier
            .padding(24.dp, 16.dp, 24.dp, 0.dp)
            .fillMaxWidth()
            .clickable(
                onClick = { navController.navigate("chatRoom/${chatRoomInfo.partyId}/${chatRoomInfo.partyName}") }
            )
    ) {
        Column() {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = chatRoomInfo.partyName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = chatRoomInfo.guildName,
                    color = Color.LightGray,
                    modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 0.dp)
                )
                Text(
                    text = "${chatRoomInfo.peopleCnt}",
                    color = Color.LightGray,
                    modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 0.dp)
                )
            }
            Text(
                text = chatRoomInfo.lastMessage ?: "아직 대화 내용이 없습니다. 대화를 시작해보세요!",
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                modifier = Modifier.padding(0.dp, 4.dp),
                style = TextStyle(
                    color = if (chatRoomInfo.lastMessage != null) {
                        Color.DarkGray // null이 아닌 경우
                    } else {
                        Color.Gray // null인 경우
                    }
                )
            )
            Text(
                text = chatRoomInfo.lastSentDate ?: "",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Right,
                color = Color.LightGray
            )
        }
    }
}