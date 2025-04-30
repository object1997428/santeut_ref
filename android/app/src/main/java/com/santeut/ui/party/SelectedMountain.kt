package com.santeut.ui.party

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapType
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.PathOverlay
import com.naver.maps.map.compose.rememberCameraPositionState
import com.santeut.R
import com.santeut.data.model.response.MountainResponse
import com.santeut.designsystem.theme.Green
import com.santeut.ui.home.SearchMountainBar
import com.santeut.ui.mountain.MountainViewModel
import com.santeut.ui.navigation.top.SimpleTopBar

@Composable
fun SelectedMountain(
    guildId: Int?,
    navController: NavController,
) {
    Column {
        SearchMountainBar(guildId, type = "create", navController)
    }
}

@Composable
fun SelectedMountainCard(
    guildId: Int?,
    navController: NavController, mountain: MountainResponse
) {
    Card(
        modifier = Modifier
            .clickable(onClick = {
                if (guildId == null) {
                    navController.navigate(
                        "create/courseList/${mountain.mountainId}/${mountain.mountainName}"
                    )
                } else {
                    navController.navigate(
                        "create/courseList/${mountain.mountainId}/${mountain.mountainName}/${guildId}"
                    )
                }
            })
            .fillMaxWidth()
            .height(100.dp)
            .padding(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = mountain.image ?: R.drawable.logo,
                contentDescription = "산 사진",
                modifier = Modifier
                    .size(100.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = mountain.mountainName,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${mountain.height}m",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = mountain.regionName,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "${mountain.courseCount}개 코스",
                    style = MaterialTheme.typography.bodySmall
                )
                if (mountain.isTop100) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = Green,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(vertical = 4.dp, horizontal = 8.dp)
                    ) {
                        Text(
                            text = "100대 명산",
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun SelectedCourse(
    guildId: Int?,
    mountainId: Int,
    mountainName: String,
    navController: NavController,
    mountainViewModel: MountainViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val pathList by mountainViewModel.pathList.observeAsState(emptyList())

    val selectedCourseIds = remember { mutableStateListOf<Int?>() }

    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(pathList) {
        if (pathList.isNotEmpty() && pathList[0].locationDataList.isNotEmpty()) {
            cameraPositionState.position = CameraPosition(
                LatLng(
                    pathList[0].locationDataList[0].lat,
                    pathList[0].locationDataList[0].lng
                ), 15.0
            )
        }
    }

    LaunchedEffect(key1 = null) {
        mountainViewModel.setPathList(mountainId)
    }

    Scaffold(
        topBar = { SimpleTopBar(navController, "$mountainName 정보") },
        content = { paddingValues ->
            NaverMap(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(vertical = 8.dp),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    locationTrackingMode = LocationTrackingMode.Follow,
                    mapType = MapType.Terrain,
                    isMountainLayerGroupEnabled = true
                )
            ) {
                pathList.forEach { courseDetail ->
                    val path = courseDetail.locationDataList.map { LatLng(it.lat, it.lng) }
                    if (path.size >= 2) {
                        val isSelected = courseDetail.courseId in selectedCourseIds

                        PathOverlay(
                            coords = path,
                            width = 3.dp,
                            color = if (isSelected) Color.Red else Color.Green,
                            outlineWidth = 1.dp,
                            outlineColor = Color.Red,
                            tag = courseDetail.courseId,
                            onClick = {
                                if (isSelected) {
                                    selectedCourseIds.remove(courseDetail.courseId)
                                } else {
                                    selectedCourseIds.add(courseDetail.courseId)
                                }
                            }
                        )
                    }
                }
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "등산로를 선택해주세요")
                    Spacer(modifier = Modifier.height(6.dp))
                    Button(onClick = {
                        val selectedCourses = selectedCourseIds.joinToString(",")
                        Log.d("등산로", selectedCourses)

                        if (selectedCourseIds.size != 0) {
                            if (guildId != null) {
                                navController.navigate("createParty/${guildId}/${mountainId}/${selectedCourses}")
                            } else {
                                navController.navigate("createParty/${mountainId}/${selectedCourses}")
                            }
                        } else {
                            Toast.makeText(context, "등산로를 1개 이상 선택해 주세요", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Text(text = "선택완료")
                    }
                }
            }
        }
    )
}