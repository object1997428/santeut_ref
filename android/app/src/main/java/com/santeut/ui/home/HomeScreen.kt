package com.santeut.ui.home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.santeut.R
import com.santeut.data.model.response.GuildResponse
import com.santeut.data.model.response.MountainResponse
import com.santeut.data.model.response.MyPartyResponse
import com.santeut.designsystem.theme.CustomGray
import com.santeut.ui.guild.GuildViewModel
import com.santeut.ui.mountain.MountainViewModel
import com.santeut.ui.party.PartyViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(
    navController: NavController,
    mountainViewModel: MountainViewModel = hiltViewModel()
) {
    Log.d("Home Screen", "Loading")

    LaunchedEffect(key1 = null) {
        mountainViewModel.popularMountain()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        item {
            SearchMountainBar(
                null,
                null,
                navController
            )
        }
        item {
            PopMountainCard(navController)
        }
        item {
            MyGuildCard(navController)
        }
        item {
            TodayHikingCard()
        }
        item {
            HomeCommunityCard(navController)
        }
    }
}

@Composable
fun SearchMountainBar(
    guildId: Int?,
    type: String?,
    navController: NavController
) {
    var name by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }

    var showToastMessage by remember { mutableStateOf(false) }
    var toastMessageText by remember { mutableStateOf<String?>(null) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            textStyle = TextStyle(fontSize = 12.sp),
            value = name,
            onValueChange = { name = it },
            placeholder = {
                Text(
                    text = "어느 산을 찾으시나요?",
                    color = Color(0xff666E7A)
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFD6D8DB),
                unfocusedContainerColor = Color(0xFFEFEFF0),
                focusedBorderColor = Color(0xFFD6D8DB),
                focusedContainerColor = Color(0xFFEFEFF0),
            ),
            shape = RoundedCornerShape(16.dp),
            trailingIcon = {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "검색",
                    tint = Color(0xff33363F),
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            if (name == null || name == "") {
                                showToastMessage = true
                                toastMessageText = "검색어를 입력하세요"
                            } else {
                                val path = if (type == "create") {
                                    if (guildId == null)
                                        "create/mountainList/$name"
                                    else
                                        "create/mountainList/$name/$guildId"
                                } else {
                                    if (region.isEmpty()) "mountainList/$name" else "mountainList/$name/$region"
                                }
                                navController.navigate(path)
                            }
                        }
                )
            }
        )
    }

    if (showToastMessage) {
        showToast(toastMessageText!!)
    }
}

@Composable
fun PopMountainCard(
    navController: NavController,
    mountainViewModel: MountainViewModel = hiltViewModel(),
) {

    val mountains by mountainViewModel.mountains.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(224.dp)
            .padding(12.dp, 12.dp, 12.dp, 0.dp),
    ) {
        Text(
            text = "산뜻에서 인기 있는 산",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .height(40.dp),
        )
        LazyRow(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
        ) {
            items(mountains) { mountain ->
                MountainCard(mountain, navController)
            }
        }
    }
}

@Composable
fun MountainCard(mountain: MountainResponse, navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(160.dp)
            .clickable(onClick = { navController.navigate("mountain/${mountain.mountainId}") }),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 0.dp, 8.dp, 0.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(100.dp)
                    .clip(shape = RoundedCornerShape(20.dp))
            ) {
                AsyncImage(
                    model = mountain.image ?: R.drawable.logo,
                    contentDescription = "산 이미지",
                    modifier = Modifier
                        .width(160.dp)
                        .height(100.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = mountain.mountainName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = mountain.regionName,
                    fontSize = 12.sp,
                    color = CustomGray
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp, start = 6.dp, end = 6.dp, bottom = 0.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${mountain.courseCount}개 코스",
                    fontSize = 12.sp,
                    color = CustomGray,
                )

                Text(
                    text = "${mountain.height}m",
                    fontSize = 12.sp,
                    color = CustomGray
                )
            }

        }
    }
}

@Composable
fun MyGuildCard(
    navController: NavController,
    guildViewModel: GuildViewModel = hiltViewModel()
) {

    val guilds by guildViewModel.guilds.observeAsState(emptyList())

    LaunchedEffect(key1 = null) {
        guildViewModel.myGuilds()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(204.dp)
            .padding(12.dp, 0.dp, 12.dp, 0.dp)
    ) {
        Row(
            modifier = Modifier
                .height(40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "내 동호회",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "전체보기",
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.LightGray,
                modifier = Modifier.clickable(onClick = { navController.navigate("guild") })
            )
        }

        if (guilds.isEmpty()) {
            Row(
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "가입한 동호회가 없습니다.")
            }
        } else {
            LazyRow(
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
            ) {
                items(guilds.take(3)) { guild ->
                    HomeGuildItem(guild,
                        onClick = { navController.navigate("getGuild/${guild.guildId}") }
                    )
                }
            }
        }
    }
}

@Composable
fun HomeGuildItem(
    guild: GuildResponse,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 0.dp, 8.dp, 0.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .height(140.dp)
                .clip(shape = RoundedCornerShape(20.dp))
        ) {
            AsyncImage(
                model = guild.guildProfile ?: R.drawable.logo,
                contentDescription = "동호회 사진",
                modifier = Modifier
                    .width(160.dp)
                    .height(140.dp),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = guild.guildName,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun TodayHikingCard(
    partyViewModel: PartyViewModel = hiltViewModel()
) {

    val todayParty by partyViewModel.myPartyList.observeAsState(emptyList())

    LaunchedEffect(key1 = null) {
        val formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        partyViewModel.getMyPartyList(
            date = formattedDate,
            includeEnd = false,
            page = null,
            size = null
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(12.dp, 12.dp, 12.dp, 0.dp)
    ) {
        Row(
            modifier = Modifier
                .height(40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "오늘의 등산 일정",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        if (todayParty.isEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "등산 일정이 없습니다.")
            }
        } else {
            LazyRow {
                items(todayParty) { party ->
                    PartyCard(party)
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
        }
    }
}

@Composable
fun PartyCard(party: MyPartyResponse) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // 일정 추가
                Image(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "달력"
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = party.schedule,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${party.partyName} ${party.guildName}",
                fontSize = 14.sp,
                modifier = Modifier.padding(24.dp, 0.dp, 0.dp, 0.dp)
            )
        }
    }
}

@Composable
fun HomeCommunityCard(
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .background(Color.LightGray, shape = RectangleShape)
    ) {
        Image(
            painter = painterResource(R.drawable.home_community_background),
            contentDescription = "home_community_background",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
        )
        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {
            Text(
                text = "커뮤니티",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
            )
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow(
                modifier = Modifier
                    .height(160.dp)
                    .fillMaxWidth()
            ) {
                item {
                    CommunityItem(
                        "산악회 회원 모집",
                        "다른 사람들과 함께 산행을 즐겨보세요!"
                    ) { navController.navigate("community/0") }
                }
                item {
                    CommunityItem(
                        "등산 소모임 모집",
                        "친구들과 산행을 즐기는 거 어때요?"
                    ) { navController.navigate("community/1") }
                }
                item {
                    CommunityItem(
                        "등산 팁 공유",
                        "등산 팁이 있으신가요?"
                    ) { navController.navigate("community/2") }
                }
                item {
                    CommunityItem(
                        "코스 공유",
                        "나만의 등산 코스를 자랑해볼까요?"
                    ) { navController.navigate("community/3") }
                }
            }
        }
    }
}

@Composable
fun CommunityItem(
    title: String, content: String, onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(140.dp)
            .padding(0.dp, 0.dp, 8.dp, 0.dp)
            .background(color = Color.White)
            .border(width = 1.dp, color = Color.Black, shape = RectangleShape)
            .clickable(onClick = { onClick() })
    ) {
        Column(
            modifier = Modifier
                .padding(6.dp)
                .height(160.dp)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = content,
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun showToast(message: String) {
    val ctx = LocalContext.current

    Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
}