package com.santeut.ui.mypage

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapType
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.PathOverlay
import com.naver.maps.map.compose.rememberCameraPositionState
import com.santeut.data.model.response.MyRecordResponse
import com.santeut.ui.party.PartyViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalNaverMapApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MyHikingScreen(
    partyViewModel: PartyViewModel = hiltViewModel()
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    val myRecordList by partyViewModel.myRecordList.observeAsState(emptyList())

    val myCourse by partyViewModel.myCourse.observeAsState(emptyList())

    LaunchedEffect(key1 = null) {
        partyViewModel.getMyRecordList()
    }

    val cameraPositionState = rememberCameraPositionState()
    LaunchedEffect(myCourse) {
        if (myCourse.isNotEmpty()) {
            cameraPositionState.position = CameraPosition(
                LatLng(
                    myCourse[0].latitude,
                    myCourse[0].longitude
                ), 15.0
            )
            showBottomSheet = true
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
                partyViewModel.initMyCourse()
            },
            scrimColor = Color.White.copy(alpha = 0.32f.coerceIn(0f, 1f)),
            sheetState = sheetState
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(text = "코스 정보")

                    if (myCourse.size < 2) {
                        Text(text = "코스 기록이 없습니다.")
                    } else {
                        NaverMap(
                            modifier = Modifier
                                .height(300.dp)
                                .width(400.dp)
                                .padding(12.dp),
                            cameraPositionState = cameraPositionState,
                            properties = MapProperties(
                                locationTrackingMode = LocationTrackingMode.Follow,
                                mapType = MapType.Terrain,
                                isMountainLayerGroupEnabled = true
                            )
                        ) {
                            PathOverlay(
                                coords = myCourse,
                                width = 3.dp,
                                color = Color.Green,
                                outlineWidth = 1.dp,
                                outlineColor = Color.Red.copy(
                                    alpha = (Color.Red.alpha.coerceIn(
                                        0f,
                                        1f
                                    ))
                                ),
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    Scaffold {
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
            LazyColumn() {
                items(myRecordList) { record ->
                    MyRecord(
                        record,
                        partyViewModel,
                        onRecordClick = {
                            partyViewModel.getMyCourse(record.partyUserId)
                            showBottomSheet = true
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MyRecord(record: MyRecordResponse, partyViewModel: PartyViewModel, onRecordClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White)
            .clip(RoundedCornerShape(4.dp))
            .clickable(onClick = onRecordClick),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
        ) {
            Row {
                Text(text = record.partyName, style = MaterialTheme.typography.headlineSmall)
                Text(text = record.guildName, style = MaterialTheme.typography.bodyMedium)
            }

            Row {
                Icon(
                    imageVector = Icons.Default.Flag,
                    contentDescription = "산 정보",
                    tint = Color.Green,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(8.dp)
                )
                Text(
                    text = record.mountainName,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Icon(
                    imageVector = Icons.Outlined.CalendarToday,
                    contentDescription = "등산 날짜",
                    tint = Color.Green,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(8.dp)
                )
                Text(
                    text = record.schedule,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "이동거리 ${record.distance ?: 0}km",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "이동시간 ${minuteToHour(record.duration ?: 0)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                text = "최고 고도 ${record.height ?: 0}m",
                style = MaterialTheme.typography.bodyMedium
            )
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
