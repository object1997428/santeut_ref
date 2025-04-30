package com.santeut.ui.community.course

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.santeut.data.model.response.MyRecordResponse
import com.santeut.ui.mypage.minuteToHour
import com.santeut.ui.party.PartyViewModel


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FindHikingHistory(
    navController: NavController,
    selectedPartyId: Int,
    onShowBottomSheet: (Boolean) -> Unit,
    partyViewModel: PartyViewModel = hiltViewModel(),
    onSelectedPartyId: (Int) -> Unit,
) {

    val myRecordList by partyViewModel.myRecordList.observeAsState(emptyList())

    // 검색어
    var searchWord by remember { mutableStateOf("") }
    
    LaunchedEffect(key1 = null) {
        partyViewModel.getMyRecordList()
    }

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Button(onClick = {
                        Log.d("userPartyId : ", selectedPartyId.toString())
                        onShowBottomSheet(false)
                    }) {
                        Text(text = "선택완료")
                    }
                }
            }
        },
        content = { paddingValues ->
            if (myRecordList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "산행 기록이 없습니다.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(20.dp))
                LazyColumn() {
                    items(myRecordList) { record ->
                        MyRecord(
                            record,
                            selectedPartyId,
                            isSelected = record.partyUserId == selectedPartyId,
                            onSelectedPartyId
                        )
                        Spacer(modifier = Modifier.height(7.dp))
                    }
                }
            }
        }
    )
}

@Composable
fun MyRecord(
    record: MyRecordResponse,
    selectedPartyId: Int,
    isSelected: Boolean,
    onPartyIdChange: (Int) -> Unit,


    ) {
    val backgroundColor by animateColorAsState(
        if (isSelected) Color(0xffDADADA).copy(alpha = 0.8f) else Color.White
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .padding(horizontal = 15.dp)
            .background(backgroundColor)
            .clickable {
                onPartyIdChange(record.partyUserId)
            }
            .clip(RoundedCornerShape(20.dp)),

        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
        ) {
            Row {
                Text(
                    text = record.partyName, style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xff335C49),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Text(text = record.guildName, style = MaterialTheme.typography.bodyMedium)
            }

            Row {
                Icon(
                    imageVector = Icons.Outlined.Flag,
                    contentDescription = "산 정보",
                    tint = Color(0xff678C40),
                    modifier = Modifier
                        .size(33.dp)
                        .padding(8.dp)
                )
                Text(
                    text = record.mountainName,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 7.dp)
                )
                Icon(
                    imageVector = Icons.Outlined.CalendarToday,
                    contentDescription = "등산 날짜",
                    tint = Color(0xff678C40),
                    modifier = Modifier
                        .size(33.dp)
                        .padding(8.dp)
                )
                Text(
                    text = record.schedule,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                Row {
                    Text(
                        text = "이동거리 ",
                        modifier = Modifier.padding(end = 10.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "${record.distance ?: 0}km",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xff678C40),
                    )
                }
                Row {
                    Text(
                        text = "이동시간",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(end = 20.dp)
                    )
                    Text(
                        text = minuteToHour(record.duration ?: 0),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xff678C40),
                    )
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row {
                Text(
                    modifier = Modifier.padding(end = 10.dp),
                    text = "최고 고도 ",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${record.height ?: 0}m",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xff678C40),
                )
            }
        }
    }
}

fun minuteToHour(duration: Int): String {
    val hour = duration / 60
    val minute = duration % 60

    if (hour == 0) {
        return "${minute}분"
    }

    if (minute == 0) {
        return "${hour}시간"
    }

    return "${hour}시간 ${minute}분"
}
