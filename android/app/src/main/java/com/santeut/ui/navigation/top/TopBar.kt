package com.santeut.ui.navigation.top

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.santeut.R
import com.santeut.data.model.response.GuildResponse
import com.santeut.ui.guild.GuildViewModel
import com.santeut.ui.party.PartyViewModel

@Composable
fun TopBar(
    navController: NavController,
    currentTap: String?
) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    when (currentTap) {
        "home" -> HomeTopBar(navController)
        "community/{initialPage}", "community" -> DefaultTopBar(navController, "커뮤니티")
        "guild" -> DefaultTopBar(navController, "나의 모임")
        "mypage" -> DefaultTopBar(navController, "마이페이지")
        "chatList" -> SimpleTopBar(navController, "채팅방")
        "noti" -> SimpleTopBar(navController, "알림")
        "mountain/{mountainId}" -> SimpleTopBar(navController, "산 정보")
        "createGuild" -> SimpleTopBar(navController, "동호회 만들기")
        "createParty" -> SimpleTopBar(navController, "소모임 만들기")
        "chatRoom/{partyId}/{partyName}" -> {
            MenuTopBar(
                navController,
                currentBackStackEntry?.arguments?.getString("partyName") ?: "소모임 제목"
            )
        }

        "map" -> SimpleTopBar_2(navController, "등산")
    }
}

@Composable
fun HomeTopBar(
    navController: NavController
) {
    TopAppBar(
        title = { Text(text = "산뜻", style = MaterialTheme.typography.titleLarge) },
        contentColor = Color.Black,
        backgroundColor = Color.White,
        navigationIcon = {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .size(50.dp)
                    .padding(start = 16.dp)
                    .clickable(onClick = { navController.navigate("home") })
            )
        },
        actions = {
            IconButton(onClick = { navController.navigate("chatList") }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Message,
                    contentDescription = "Message"
                )
            }
            IconButton(onClick = { navController.navigate("noti") }) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications"
                )
            }
        }
    )
}

@Composable
fun DefaultTopBar(navController: NavController, pageName: String) {
    TopAppBar(
        title = { Text(pageName, style = MaterialTheme.typography.titleLarge) },
        contentColor = Color.Black,
        backgroundColor = Color.White,
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(onClick = { navController.navigate("chatList") }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Message,
                    contentDescription = "Message"
                )
            }
            IconButton(onClick = { navController.navigate("noti") }) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications"
                )
            }
        }
    )
}

@Composable
fun SimpleTopBar(navController: NavController, pageName: String) {
    TopAppBar(
        title = { Text(pageName, style = MaterialTheme.typography.titleLarge) },
        contentColor = Color.Black,
        backgroundColor = Color.White,
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "Back"
                )
            }
        }
    )
}

@Composable
fun SimpleTopBar_2(navController: NavController, pageName: String) {
    TopAppBar(
        title = { Text(pageName, style = MaterialTheme.typography.titleLarge) },
        contentColor = Color.Black,
        backgroundColor = Color.White,
        navigationIcon = {
            IconButton(onClick = {
                navController.navigate("home") {
                    popUpTo(0) { inclusive = true }
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "Back"
                )
            }
        }
    )
}

@Composable
fun MenuTopBar(
    navController: NavController,
    pageName: String,
    partyViewModel: PartyViewModel = hiltViewModel()
) {

    var showMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(pageName, style = MaterialTheme.typography.titleLarge) },
        contentColor = Color.Black,
        backgroundColor = Color.White,
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "Back"
                )
            }
        },
        actions = {

            var showDialog by remember { mutableStateOf(false) }

            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "추가 메뉴"
                )
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text(text = "소모임 정보", style = MaterialTheme.typography.titleMedium) },
                    onClick = { Log.d("소모임 정보", "클릭") }
                )

                DropdownMenuItem(
                    text = {
                        Text(
                            text = "소모임 나가기",
                            color = Color.Red,
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    onClick = { showDialog = true })
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    text = {
                        Text(
                            text = "${pageName}에서 나가시겠습니까?",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                Log.d("소모임", "나감")
                                showDialog = false
                            }
                        ) {
                            Text("나가기", style = MaterialTheme.typography.titleMedium)
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDialog = false }) {
                            Text("취소", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                )
            }
        }
    )
}

@Composable
fun CreateTopBar(navController: NavController, pageName: String, onWriteClick: () -> Unit) {
    TopAppBar(
        title = { Text(pageName, style = MaterialTheme.typography.titleLarge) },
        contentColor = Color.Black,
        backgroundColor = Color.White,
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(onClick = onWriteClick) {
                Icon(
                    imageVector = Icons.Outlined.Create,
                    contentDescription = "글쓰기"
                )
            }
        }
    )
}

@Composable
fun GuildTopBar(
    navController: NavController,
    guild: GuildResponse,
    guildViewModel: GuildViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    var showMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(guild.guildName, style = MaterialTheme.typography.titleLarge) },
        contentColor = Color.Black,
        backgroundColor = Color.White,
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIosNew,
                    contentDescription = "Back"
                )
            }
        },
        actions = {

            var showDialog by remember { mutableStateOf(false) }
            var showLinkModal by remember { mutableStateOf(false) }

            IconButton(onClick = {
                showLinkModal = true
            }) {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = "링크 공유"
                )
            }
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "추가 메뉴"
                )
            }
            if (showLinkModal) {
                Dialog(
                    onDismissRequest = { showLinkModal = false },
                    content = {
                        Column(
                            Modifier
                                .size(300.dp, 120.dp)
                                .background(Color.White, shape = RoundedCornerShape(20.dp))
                                .padding(horizontal = 20.dp),
                            verticalArrangement = Arrangement.Center, // 수직으로 중앙 정렬
                            horizontalAlignment = Alignment.CenterHorizontally // 수평으로 중앙 정렬

                        )
                        {
                            Text(
                                text = "${guild.guildName}의 공유 링크",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                modifier = Modifier.padding(bottom = 10.dp),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(
                                            Color(0xffEFEFF0),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 8.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = "https://k10e201.p.ssafy.io/hi.html?param=" + guild.guildId,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier
                                            .align(Alignment.CenterStart),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.Link,
                                    contentDescription = "복사",
                                    modifier = Modifier
                                        .size(35.dp)
                                        .padding(start = 8.dp)
                                        .clickable {
                                            val clip = ClipData.newPlainText(
                                                "길드 공유링크 클립보드에 복사",
                                                "https://k10e201.p.ssafy.io/hi.html?param=" + guild.guildId
                                            )
                                            clipboardManager.setPrimaryClip(clip)
                                        },
                                    tint = Color(0xff76797D)
                                )
                            }
                        }
                    },
                )
            }


            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "회원 목록 보기",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    onClick = { navController.navigate("guildMemberList/${guild.guildId}") })
                DropdownMenuItem(text = {
                    Text(
                        text = "소모임 만들기",
                        style = MaterialTheme.typography.titleMedium
                    )
                }, onClick = {
                    navController.navigate("createParty/${guild.guildId}")
                })

                if (guild.isPresident) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "가입 요청 보기",
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        onClick = { navController.navigate("guildApplyList/${guild.guildId}") })
                    DropdownMenuItem(text = {
                        Text(
                            text = "동호회 관리",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }, onClick = {
                        navController.navigate("updateGuild/${guild.guildId}")
                    })
                }

                DropdownMenuItem(
                    text = {
                        Text(
                            text = "동호회 탈퇴하기",
                            color = Color.Red,
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    onClick = { showDialog = true })
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    text = {
                        Text(
                            text = "${guild.guildName}을 탈퇴할까요?",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                guildViewModel.quitGuild(guild.guildId)
                                showDialog = false
                            }
                        ) {
                            Text("탈퇴", style = MaterialTheme.typography.titleMedium)
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDialog = false }) {
                            Text("취소", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                )
            }
        }
    )
}


@Composable
fun CustomAlertDialog(
    showDialog: MutableState<Boolean>,
    guildName: String,
    onConfirmAction: () -> Unit,
    onDismissAction: () -> Unit
) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = {
                Text(
                    text = "공유 링크", style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Column {
                    Text("공유하고 싶은 링크를 선택하세요.", style = MaterialTheme.typography.titleMedium)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirmAction()
                        showDialog.value = false
                    }
                ) {
                    Text("확인", style = MaterialTheme.typography.titleMedium)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        onDismissAction()
                        showDialog.value = false
                    }
                ) {
                    Text("취소", style = MaterialTheme.typography.titleMedium)
                }
            }
        )
    }
}
