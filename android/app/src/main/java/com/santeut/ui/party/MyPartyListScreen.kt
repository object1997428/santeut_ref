package com.santeut.ui.party

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.outlined.CalendarMonth

import androidx.compose.material.icons.outlined.KeyboardDoubleArrowUp
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.santeut.data.model.response.MyPartyResponse
import com.santeut.designsystem.theme.DarkGreen
import com.santeut.designsystem.theme.Green
import com.santeut.ui.map.MapViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Composable
fun MyPartyListScreen(
    navController: NavController,
    partyViewModel: PartyViewModel = hiltViewModel(),
    mapViewModel: MapViewModel
) {
    val myPartyList by partyViewModel.myPartyList.observeAsState(emptyList())

    LaunchedEffect(key1 = null) {
        partyViewModel.getMyPartyList(date = null, includeEnd = false, page = null, size = null)
    }

    val startHikingPartyId by partyViewModel.startHikingPartyId
    val distance by partyViewModel.distance
    val courseList by partyViewModel.courseList

    LaunchedEffect(startHikingPartyId) {
        if (startHikingPartyId != 0) {
            mapViewModel.setHikingData(
                startHikingPartyId,
                distance,
                courseList
            )
            navController.navigate("map")
        }
    }

    Column {

        if (myPartyList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "가입된 소모임이 없습니다.",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize()
            ) {
                items(myPartyList) { party ->
                    MyPartyCard(party, navController, partyViewModel, mapViewModel)
                }
            }
        }
    }
}


@Composable
fun MyPartyCard(
    party: MyPartyResponse,
    navController: NavController,
    partyViewModel: PartyViewModel,
    mapViewModel: MapViewModel
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(0.7f)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row {
                        Text(
                            text = party.partyName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkGreen
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                        Text(
                            text = party.guildName,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Gray
                        )
                    }
                }
                // 일정
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(0.dp, 4.dp, 0.dp, 0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CalendarMonth,
                        contentDescription = "일정",
                        tint = Green,
                        modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)
                    )
                    Text(text = party.schedule)
                }
                // 모임장소
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(0.dp, 4.dp, 0.dp, 0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = "위치",
                        tint = Green,
                        modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)
                    )
                    Text(text = party.place)
                }
                // 산, 인원수
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(0.dp, 4.dp, 0.dp, 0.dp)
                        .fillMaxWidth()
                ) {
                    Row {
                        Icon(
                            imageVector = Icons.Outlined.KeyboardDoubleArrowUp,
                            contentDescription = "산",
                            tint = Green,
                            modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)
                        )
                        Text(text = party.mountainName)
                    }
                    Row {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "인원 수",
                            tint = Green,
                            modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp)
                        )
                        Text(text = "${party.curPeople} / ${party.maxPeople} 명")
                    }
                }
            }
            Column(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(0.3f)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (party.status == "P") {
                        Icon(
                            imageVector = Icons.Default.Sensors,
                            contentDescription = "진행 중",
                            tint = Color.Red,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(0.7f)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if ((party.status == "P") || (party.status == "B" && isMeetingTimePassed(party.schedule))) {
                        Button(
                            onClick = {
                                partyViewModel.startHiking(party.partyId)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Green),
                            modifier = Modifier.size(36.dp),
                            contentPadding = PaddingValues(0.dp),
                            shape = RoundedCornerShape(50)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "시작 버튼",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    } else {
                        Button(
                            onClick = {
                                Toast.makeText(context, "시작할 수 없는 소모임입니다.", Toast.LENGTH_SHORT)
                                    .show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                            modifier = Modifier.size(36.dp),
                            contentPadding = PaddingValues(0.dp),
                            shape = RoundedCornerShape(50)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "시작 버튼",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


fun isMeetingTimePassed(meetingTime: String): Boolean {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val meetingDateTime = LocalDateTime.parse(meetingTime, formatter)
    val currentDateTime = LocalDateTime.now()
    return currentDateTime.isAfter(meetingDateTime)
}