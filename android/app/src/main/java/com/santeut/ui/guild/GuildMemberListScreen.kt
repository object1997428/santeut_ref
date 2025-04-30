package com.santeut.ui.guild

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.santeut.R
import com.santeut.data.model.response.GuildMemberResponse
import com.santeut.data.model.response.GuildResponse
import com.santeut.designsystem.theme.Green
import com.santeut.designsystem.theme.Red
import com.santeut.ui.navigation.top.GuildTopBar
import java.time.format.DateTimeFormatter

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GuildMemberListScreen(
    guildId: Int,
    navController: NavController,
    guildViewModel: GuildViewModel = hiltViewModel()
) {

    val guild by guildViewModel.guild.observeAsState()
    val memberList by guildViewModel.memberList.observeAsState(emptyList())

    LaunchedEffect(key1 = guildId) {
        guildViewModel.getGuild(guildId)
        guildViewModel.getGuildMemberList(guildId)
    }

    Scaffold(
        topBar = {
            guild?.let { guild ->
                GuildTopBar(navController, guild)
            }
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxHeight()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "동호회 회원",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    )
                    Text(
                        text = "${memberList.size}명",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    )
                }
                Divider(
                    color = Color.LightGray,
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                )
                LazyColumn() {
                    items(memberList) { member ->
                        guild?.let { MemberRow(it, member) }
                    }
                }
            }
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberRow(
    guild: GuildResponse,
    member: GuildMemberResponse,
    guildViewModel: GuildViewModel = hiltViewModel()
) {

    // bottom sheet
    var showBottomSheet by remember { mutableStateOf(false) }
    val BottomSheetState = rememberModalBottomSheetState()

    // alert dialog
    var showExileDialog = remember { mutableStateOf(false) }
    var showChangeDialog = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .clickable { showBottomSheet = true },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = member.userProfile ?: R.drawable.logo,
                contentDescription = "회원 프로필 사진",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .border(2.dp, Green, CircleShape)
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = member.userNickname,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
        }

        if (guild.isPresident && showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = BottomSheetState,
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 사진
                    AsyncImage(
                        model = member.userProfile ?: R.drawable.logo,
                        contentDescription = "회원 프로필 사진",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .border(2.dp, Green, CircleShape)
                    )
                    Spacer(Modifier.height(8.dp))
                    // 닉네임
                    Text(
                        text = member.userNickname,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    // 가입일
                    Text(
                        text = "가입일 ${member.joinDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}",
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )

                }
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                ) {
                    // 위임 버튼
                    FilledTonalButton(
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Green
                        ),
                        shape = RoundedCornerShape(12.dp),
                        onClick = {
                            showChangeDialog.value = true
                        }) {
                        Text(
                            text = "위임하기",
                            color = Color.White
                        )

                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    // 추방 버튼
                    FilledTonalButton(
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Red
                        ),
                        shape = RoundedCornerShape(12.dp),
                        onClick = {
                            showExileDialog.value = true
                        }) {
                        Text(
                            text = "추방하기",
                            color = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
            }

            if (showChangeDialog.value) {
                showAlertDialog(
                    showDialog = showChangeDialog,
                    type = AlertType.CHANGE,
                    text = "${member.userNickname}님에게 ${guild.guildName} 회장을 위임하시겠습니까?",
                    member = member,
                    guild = guild,
                    guildViewModel = guildViewModel
                )
            } else if (showExileDialog.value) {
                showAlertDialog(
                    showDialog = showExileDialog,
                    type = AlertType.EXILE,
                    text = "${member.userNickname}님을 추방하시겠습니까?",
                    member = member,
                    guild = guild,
                    guildViewModel = guildViewModel
                )
            }
        }
    }
}

@Composable
fun showAlertDialog(
    showDialog: MutableState<Boolean>,//Boolean,
    type: AlertType,
    text: String,
    member: GuildMemberResponse,
    guild: GuildResponse,
    guildViewModel: GuildViewModel
) {
    if (showDialog.value == true) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            text = { Text(text = text) }, // "${member.userNickname}님을 추방할까요?"
            confirmButton = {
                Button(
                    onClick = {
                        when (type) {
                            AlertType.EXILE -> guildViewModel.exileMember(
                                guild.guildId,
                                member.userId
                            )

                            AlertType.CHANGE -> guildViewModel.changeLeader(
                                guild.guildId,
                                member.userId
                            )
                        }
                        showDialog.value = false
                    }
                ) {
                    Text("네")
                }
            },
            dismissButton = {
                Button(onClick = {
                    showDialog.value = false
                }) {
                    Text("아니요")
                }
            }
        )
    }
}

enum class AlertType {
    EXILE, // 추방
    CHANGE // 위임
}