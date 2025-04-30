package com.santeut.ui.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapType
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.PathOverlay
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberFusedLocationSource
import com.santeut.R
import com.santeut.ui.wearable.WearableViewModel
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@ExperimentalNaverMapApi
@Composable
fun MapScreen(
    mapViewModel: MapViewModel,
    wearableViewModel: WearableViewModel,
    onNavigateSearchPlant: () -> Unit
) {
    val context = LocalContext.current

    val partyId by mapViewModel.partyId

    val myLocation by mapViewModel.myLocation
    val cameraPositionState = rememberCameraPositionState {
        position = myLocation?.let { CameraPosition(it, 15.0) }!!
    }

    val courseList by mapViewModel.courseList

    val userPosition by mapViewModel.userPositions
    val userIcons by mapViewModel.userIcons

    LaunchedEffect(userPosition) {
        wearableViewModel.toSend(userPosition)
    }

    val defaultIcon =
        remember { mapViewModel.resizeMarkerIcon(context, R.drawable.logo, 100, 100) }

    val uiSettings = remember {
        MapUiSettings(
            isZoomControlEnabled = true,
            isLocationButtonEnabled = true,
            isCompassEnabled = true
        )
    }

    // 기록
    val distance by mapViewModel.distance
    val startTime by mapViewModel.startTime
    val stepCount by mapViewModel.stepCount
    val movedDistance by mapViewModel.movedDistance
    val altitude by mapViewModel.altitude
    val calorie by mapViewModel.calorie
    val heartRate by mapViewModel.heartRate

    // 임시
    val alertTitle by mapViewModel.alertTitle
    val alertMessage by mapViewModel.alertMessage
    val deviation by mapViewModel.deviation

    // 헬스...
    val healthData by wearableViewModel.healthData

    LaunchedEffect(healthData) {
        mapViewModel.updateHealthData(healthData)
    }

    LaunchedEffect(alertMessage, alertTitle) {
        if (alertMessage.isNotBlank() && alertTitle.isNotBlank()) {
            wearableViewModel.sendAlertMessage(alertTitle, alertMessage)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(1f),
            contentAlignment = Alignment.BottomCenter
        ) {
            NaverMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                locationSource = rememberFusedLocationSource(isCompassEnabled = uiSettings.isCompassEnabled),
                properties = MapProperties(
                    locationTrackingMode = LocationTrackingMode.Follow,
                    mapType = MapType.Terrain,
                    isMountainLayerGroupEnabled = true
                ),
                uiSettings = uiSettings
            ) {
                // 등산 코스 경로
                if (courseList.isNotEmpty()) {
                    PathOverlay(
                        coords = courseList,
                        width = 3.dp,
                        color = Color.Green,
                        outlineWidth = 1.dp,
                        outlineColor = Color.Red,
                    )
                }

                userPosition.forEach { (userNickname, position) ->
                    val icon = userIcons[userNickname] ?: defaultIcon
                    Marker(
                        state = MarkerState(position = position),
                        captionText = userNickname,
                        captionTextSize = 14.sp,
                        captionMinZoom = 12.0,
                        icon = icon
                    )
                }
            }
            if (partyId == 0) {
                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .width(220.dp)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "참여중인 소모임이 없습니다.",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            } else {
                Button(
                    onClick = { mapViewModel.endedHiking() },
                    modifier = Modifier
                        .height(60.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .padding(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF678C40),
                        contentColor = Color.White,
                    )
                ) {
                    Text(
                        text = "등산 종료",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }
            }

            Button(
                onClick = {
                    onNavigateSearchPlant()
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Camera,
                    contentDescription = "Camera",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        if (alertTitle.isNotBlank() && alertMessage.isNotBlank()) {
            AlertDialog(
                onDismissRequest = { mapViewModel.checkAlertMessage() },
                title = { Text(text = alertTitle) },
                text = { Text(text = alertMessage) },
                confirmButton = {
                    Button(onClick = { mapViewModel.checkAlertMessage() }) {
                        Text(
                            text = "확인"
                        )
                    }
                }
            )
        }
        // 인포 창
        if (partyId != 0) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(185.dp)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                HikingInfoPanel(
                    startTime,
                    stepCount,
                    movedDistance,
                    altitude,
                    calorie,
                    distance - movedDistance,
                    heartRate
                )
            }
        }
    }
}


@Composable
fun HikingInfoPanel(
    startTime: LocalDateTime?,
    stepCount: Int,
    movedDistance: Double,
    altitude: Int,
    calorie: Int,
    remainDistance: Double,
    heartRate: Int
) {
    var elapsedTime by remember { mutableStateOf("") }

    var formattedTime by remember { mutableStateOf("00:00:00") }

    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    LaunchedEffect(startTime) {
        while (true) {
            if (startTime != null) {
                val now = LocalDateTime.now()
                val duration = ChronoUnit.SECONDS.between(startTime, now)
                val hours = duration / 3600
                val minutes = (duration % 3600) / 60
                val seconds = duration % 60

                formattedTime =
                    LocalDateTime.of(0, 1, 1, hours.toInt(), minutes.toInt(), seconds.toInt())
                        .format(formatter)
                elapsedTime = formattedTime
            } else {
                elapsedTime = "00:00:00"
            }
            delay(1000L)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(6.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = formattedTime,
            color = Color.Black
        )
        Row(

        ) {
            HikingInfoItem(
                title = "걸음수",
                value = stepCount.toString(),
                unit = "걸음"
            )
            HikingInfoItem(
                title = "이동거리",
                value = String.format("%.2f", movedDistance),
                unit = "km"
            )
            HikingInfoItem(
                title = "고도",
                value = altitude.toString(),
                unit = "m"
            )
        }
        Row(

        ) {
            HikingInfoItem(
                title = "칼로리",
                value = calorie.toString(),
                unit = "kcal"
            )
            HikingInfoItem(
                title = "남은거리",
                value = String.format("%.2f", remainDistance),
                unit = "km"
            )
            HikingInfoItem(
                title = "심박수",
                value = heartRate.toString(),
                unit = "bpm"
            )
        }
    }
}

@Composable
fun HikingInfoItem(
    title: String,
    value: String,
    unit: String
) {
    Column(
        modifier = Modifier
            .height(70.dp)
            .width(120.dp)
            .padding(2.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFE5DD90)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = title, color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Normal)
        Text(text = value, color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        Text(text = unit, color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Normal)
    }
}