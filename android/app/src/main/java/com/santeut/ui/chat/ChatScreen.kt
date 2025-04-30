package com.santeut.ui.chat

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.santeut.R
import com.santeut.data.model.response.ChatMessage
import com.santeut.designsystem.theme.DarkGreen
import com.santeut.designsystem.theme.Green
import com.santeut.ui.mypage.UserViewModel


@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun ChatScreen(
    partyId: Int,
    chatViewModel: ChatViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {

    val myProfile by userViewModel.myProfile.observeAsState()
    val chatmessages by chatViewModel.chatmessages.observeAsState(emptyList())
    val listState = rememberLazyListState()

    LaunchedEffect(key1 = partyId, key2 = chatmessages.size) {
        chatViewModel.getChatMessageList(partyId)
        chatViewModel.connect(partyId)
        if (chatmessages.isNotEmpty()) {
            listState.scrollToItem(chatmessages.size - 1)
        }
        userViewModel.getMyProfile()
    }

    DisposableEffect(Unit) {
        onDispose {
            chatViewModel.disconnect()
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize(),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Surface(
                    modifier = Modifier.weight(1f)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth(),
                        reverseLayout = false,
                        state = listState
                    ) {
                        chatmessages.forEach {
                            item {
                                if (it.userNickname == (myProfile?.userNickname ?: "")) {
                                    MyChatMessage(message = it)
                                } else {
                                    OthersChatMessage(message = it)
                                }
                            }
                        }
                    }
                }
                ChatBox(chatViewModel)
            }
        }
    )
}

@Composable
fun MyChatMessage(message: ChatMessage) {
    Row(
        modifier = Modifier
            .padding(16.dp, 8.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp, 0.dp, 0.dp, 0.dp)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.End)
                    .clip(
                        RoundedCornerShape(
                            topStart = 48f,
                            topEnd = 48f,
                            bottomStart = 48f,
                            bottomEnd = 0f
                        )
                    )
                    .background(DarkGreen)
                    .padding(16.dp)
            ) {
                Text(
                    text = message.content,
                    color = Color.White,
                    softWrap = true
                )
            }

            Text(
                message.createdAt,
                color = Color.LightGray,
                modifier = Modifier
                    .padding(4.dp, 4.dp, 0.dp, 0.dp)
                    .align(Alignment.End)
            )
        }

    }
}

@Composable
fun OthersChatMessage(message: ChatMessage) {
    Row(
        modifier = Modifier
            .padding(16.dp, 8.dp)
            .fillMaxWidth()
    ) {
        if (message.userProfile != null && message.userProfile.equals("null")) {
            message.userProfile.let { message.userProfile = null }
        }
        AsyncImage(
            model = message.userProfile ?: R.drawable.logo,
            contentDescription = "프로필 이미지",
            modifier = Modifier
                .width(40.dp)
                .height(40.dp)
                .clip(CircleShape)
                .border(
                    BorderStroke(1.dp, Green),
                    CircleShape
                ),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .padding(8.dp, 0.dp, 0.dp, 0.dp)
        ) {
            Text(
                message.userNickname,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 4.dp)
            )
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 48f,
                            topEnd = 48f,
                            bottomStart = 0f,
                            bottomEnd = 48f
                        )
                    )
                    .background(Color.LightGray)
                    .padding(16.dp)
            ) {
                Text(
                    text = message.content,
                    color = Color.Black,
                    softWrap = true
                )
            }

            Text(
                message.createdAt,
                color = Color.LightGray,
                modifier = Modifier.padding(4.dp, 4.dp, 0.dp, 0.dp)
            )
        }

    }
}

@Composable
fun ChatBox(
    chatViewModel: ChatViewModel
) {
    var chatBoxValue by remember { mutableStateOf(TextFieldValue("")) }
    var chatBoxValueHasValue = chatBoxValue.text.isNotEmpty()

    OutlinedTextField(
        modifier = Modifier
            .padding(8.dp, 4.dp)
            .fillMaxWidth(),
        value = chatBoxValue,
        onValueChange = { chatBoxValue = it },
        shape = CircleShape,
        trailingIcon = {
            IconButton(
                onClick = {
                    if (chatBoxValueHasValue) {
                        // send
                        Log.d("SEND", chatBoxValue.text)
                        chatViewModel.sendMessage(
                            Message(
                                chatBoxValue.text,
                                "message"
                            )
                        )
                        // 채팅창 내용 초기화
                        chatBoxValue = TextFieldValue("")
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = null
                )
            }
        }
    )

}
