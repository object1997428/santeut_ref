@file:OptIn(ExperimentalNaverMapApi::class)

package com.santeut.ui.mountain

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
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
import com.santeut.data.apiservice.WeatherApi
import com.santeut.data.model.response.CourseDetailResponse
import com.santeut.data.model.response.HikingCourseResponse
import com.santeut.data.model.response.MountainDetailResponse
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun MountainScreen(
    mountainId: Int,
    mountainViewModel: MountainViewModel = hiltViewModel()
) {

    val pages = listOf("등산 코스", "날씨")
    var selectedTab by remember { mutableIntStateOf(0) }

    val mountain by mountainViewModel.mountain.observeAsState()
    val courseList by mountainViewModel.courseList.observeAsState(emptyList())
    val pathData by mountainViewModel.pathList.observeAsState(listOf())

    LaunchedEffect(key1 = mountainId) {
        mountainViewModel.mountainDetail(mountainId)
        mountainViewModel.getHikingCourseList(mountainId)
        mountainViewModel.setPathList(mountainId)
    }

    Scaffold {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            item {
                AsyncImage(
                    model = mountain?.image ?: R.drawable.logo,
                    contentDescription = "산 이미지",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }

            item { MountainDetail(mountain) }

            item {
                TabRow(
                    selectedTabIndex = selectedTab
                ) {
                    pages.forEachIndexed { index, title ->
                        Tab(
                            text = { Text(title) },
                            selected = selectedTab == index,
                            onClick = { selectedTab = index }
                        )
                    }
                }
            }

            when (selectedTab) {
                0 -> item { HikingCourse(mountain, courseList, pathData) }
                1 -> item { MountainWeather(mountain) }
            }
        }
    }
}

@Composable
fun MountainDetail(mountain: MountainDetailResponse?) {
    if (mountain != null) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row {
                Text(
                    text = mountain.mountainName ?: "",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "${mountain.height ?: 0}m",
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.Bottom)
                )
            }
            Text(
                text = mountain.address ?: "",
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = mountain.description ?: "",
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun HikingCourse(
    mountain: MountainDetailResponse?,
    courseList: List<HikingCourseResponse>,
    pathData: List<CourseDetailResponse>
) {
    val courseCount = mountain?.courseCount ?: 0

    val cameraPositionState = rememberCameraPositionState {
        mountain?.let {
            position = CameraPosition(LatLng(it.lat, it.lng), 11.0)
        }
    }

    LaunchedEffect(mountain) {
        mountain?.let {
            cameraPositionState.position = CameraPosition(LatLng(it.lat, it.lng), 11.0)
        }
    }


    Column(
        modifier = Modifier.padding(20.dp)
    ) {

        Box(
            modifier = Modifier
                .height(232.dp)
                .fillMaxWidth()
                .background(Color(0xFFD9D9D9))
                .shadow(4.dp, RoundedCornerShape(4.dp)),
        ) {
            NaverMap(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    locationTrackingMode = LocationTrackingMode.Follow,
                    mapType = MapType.Terrain,
                    isMountainLayerGroupEnabled = true
                )
            ) {
                pathData.forEach { courseDetail ->
                    val path = courseDetail.locationDataList.map { LatLng(it.lat, it.lng) }
                    if (path.size >= 2) {
                        PathOverlay(
                            coords = path,
                            width = 3.dp,
                            color = Color.Green,
                            outlineWidth = 1.dp,
                            outlineColor = Color.Red,
                            tag = courseDetail.courseId
                        )
                    }
                }
            }
        }

        Column {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                Text(
                    text = "등산로",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 24.sp,
                    fontFamily = FontFamily.SansSerif
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${courseCount}개",
                    color = Color.Black,
                    fontSize = 14.sp,
                    lineHeight = 16.sp,
                    textAlign = TextAlign.Right,
                )
            }

            courseList.forEach { course ->
                CourseItem(
                    course = course,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

        }
    }
}

@Composable
fun CourseItem(course: HikingCourseResponse, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(
                color = Color(0xFFE5DD90),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(8.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            backgroundColor = Color.Transparent,
            elevation = 0.dp
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .background(color = Color(0xFFE5DD90))
                        .fillMaxWidth()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${course.courseName ?: ""} 코스",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = Color.Black,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("${course.distance ?: "?"}km")
                            }
                        }
                    )
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Color.Gray)) {
                                append("난이도 ")
                            }
                            withStyle(style = SpanStyle(color = getColorForLevel(course.level))) {
                                append(course.level ?: "알 수 없음")
                            }
                        }
                    )
                }

                Row(
                    modifier = Modifier
                        .background(color = Color(0xFFE5DD90))
                        .fillMaxWidth()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Color.Gray)) {
                                append("등산 시간 ")
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = Color.Black,
                                )
                            ) {
                                append("${course.upTime ?: "?"}분")
                            }
                        }
                    )
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Color.Gray)) {
                                append("하산 시간 ")
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = Color.Black,
                                )
                            ) {
                                append("${course.downTime ?: "?"}분")
                            }
                        },
                        modifier = Modifier.padding(end = 20.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun getColorForLevel(level: String?): Color {
    return when (level) {
        "쉬움" -> Color(0xFF335C49)
        "보통" -> Color.Black
        "어려움" -> Color.Red
        else -> Color.Gray
    }
}

class WeatherApiViewModel : ViewModel() {
    private val _currentWeather = MutableLiveData<CurrentWeather>()
    val currentWeather: LiveData<CurrentWeather> = _currentWeather

    private val _dailyWeather = MutableLiveData<List<DailyWeather>>()
    val dailyWeather: LiveData<List<DailyWeather>> = _dailyWeather

    private val _hourlyWeather = MutableLiveData<List<HourlyWeather>>()
    val hourlyWeather: LiveData<List<HourlyWeather>> = _hourlyWeather

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val weatherApi = retrofit.create(WeatherApi::class.java)
    private val apiKey = "ec73a8bf74a350c22c3659fd6c371854"

    fun getWeatherInfo(lat: Double, lon: Double) {
        Log.i("getWeatherInfo", "getWeatherInfo: $lat, $lon")
        viewModelScope.launch {
            try {
                val response = weatherApi.getWeather(
                    lat,
                    lon,
                    "minutely",
                    "metric",
                    apiKey
                )
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string()
                    Log.d("API Response", "Success: ${responseBody}")

                    // JSON 객체 파싱
                    val jsonObject = JSONObject(responseBody)

                    //[current 추출] 현재
                    val currentObject = jsonObject.getJSONObject("current")
                    val weatherArray = currentObject.getJSONArray("weather")
                    val icon = if (weatherArray.length() > 0) weatherArray.getJSONObject(0)
                        .getString("icon") else "No icon"
                    val temp = Math.round(currentObject.getDouble("temp")).toInt()
                    Log.d("Current Weather", "temp: $temp")
                    Log.d("Current Weather", "weather.icon: $icon")
                    _currentWeather.postValue(CurrentWeather(temp, icon))

                    //[daily 추출]현재요일부터 7일
                    val dailylist = mutableListOf<DailyWeather>()
                    val dailyArray = jsonObject.getJSONArray("daily")
                    for (i in 0 until dailyArray.length() - 1) {
                        val dailyObject = dailyArray.getJSONObject(i)
                        val dateTimestamp = dailyObject.getLong("dt")
                        val formattedDate = formatDate(dateTimestamp)
                        val parts = formattedDate.split(" ")
                        val datePart = parts[0]
                        val dayPart = parts[1]

                        val tempObject = dailyObject.getJSONObject("temp")
                        val weatherArray = dailyObject.getJSONArray("weather")
                        // 각 요소의 값을 가져옴
                        val tempMin = Math.round(tempObject.getDouble("min")).toInt()
                        val tempMax = Math.round(tempObject.getDouble("max")).toInt()
                        val humidity = dailyObject.getInt("humidity")
                        val icon = if (weatherArray.length() > 0) weatherArray.getJSONObject(0)
                            .getString("icon") else "No icon"
                        dailylist.add(
                            DailyWeather(
                                datePart,
                                dayPart,
                                tempMin,
                                tempMax,
                                humidity,
                                icon
                            )
                        )
                        _dailyWeather.postValue(dailylist)
                    }
                    //[hourly 추출]현재요일부터 7일
                    val hourlylist = mutableListOf<HourlyWeather>()
                    val hourlyArray = jsonObject.getJSONArray("hourly")
                    for (i in 0 until hourlyArray.length() step 3) {
                        if (i < 24) {
                            val hourlyObject = hourlyArray.getJSONObject(i)
                            val dateTimestamp = hourlyObject.getLong("dt")
                            val formattedDate = formatTime(dateTimestamp)
                            val weatherArray = hourlyObject.getJSONArray("weather")
                            val icon = if (weatherArray.length() > 0) weatherArray.getJSONObject(0)
                                .getString("icon") else "No icon"
                            val temp = Math.round(hourlyObject.getDouble("temp")).toInt()
                            hourlylist.add(
                                HourlyWeather(
                                    formattedDate,
                                    temp,
                                    icon
                                )
                            )
                        }
                    }
                    _hourlyWeather.postValue(hourlylist)
                } else {
                    Log.d("API Response", "Failure: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.d("API Response", "Error: ${e.message}")
            }
        }
    }
}

data class CurrentWeather(
    val temperature: Int,
    val iconUrl: String
)

data class DailyWeather(
    val date: String,
    val day: String,
    val minTemp: Int,
    val maxTemp: Int,
    val humidity: Int,
    val iconUrl: String
)

data class HourlyWeather(
    val time: String,
    val temperature: Int,
    val iconUrl: String
)

fun formatDate(timestamp: Long): String {
    val instant = Instant.ofEpochSecond(timestamp)
    val zoneId = ZoneId.of("Asia/Seoul")
    val formatter = DateTimeFormatter.ofPattern("MM/dd EEE")
        .withLocale(Locale.KOREAN)
    return formatter.format(instant.atZone(zoneId).toLocalDate())
}

fun formatTime(timestamp: Long): String {
    val instant = Instant.ofEpochSecond(timestamp)
    val zoneId = ZoneId.of("Asia/Seoul")
    val formatter = DateTimeFormatter.ofPattern("HH")
        .withLocale(Locale.KOREAN)
    return formatter.format(instant.atZone(zoneId).toLocalDateTime())
}

@Composable
fun MountainWeather(mountain: MountainDetailResponse?) {
    val context = LocalContext.current
    val weatherApiViewModel: WeatherApiViewModel =
        viewModel()
    val currentWeather by weatherApiViewModel.currentWeather.observeAsState()
    val dailyWeather by weatherApiViewModel.dailyWeather.observeAsState()
    val hourlyWeather by weatherApiViewModel.hourlyWeather.observeAsState()

    if (mountain != null) {
        Log.i("mountain", "MountainWeather: $mountain")

        LaunchedEffect(key1 = mountain) {
            weatherApiViewModel.getWeatherInfo(mountain.lat, mountain.lng)
        }

        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            Card(
                modifier = Modifier
                    .height(310.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .background(
                                color = Color.White,
                            )
                    ) {
                        val iconName = "weather_" + (currentWeather?.iconUrl?.dropLast(1) ?: "01")
                        // 왼쪽 섹션
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            val resourceId = context.resources.getIdentifier(
                                iconName,
                                "drawable",
                                context.packageName
                            )
                            Image(
                                painter = painterResource(id = resourceId),
                                contentDescription = "Weather Icon",
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(top = 20.dp, bottom = 20.dp, start = 30.dp)
                            )
                        }
                        // 오른쪽 섹션
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(vertical = 33.dp, horizontal = 10.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = getWeatherDescription(iconName),
                                    color = Color.Black,
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    style = TextStyle(
                                        fontSize = 13.sp,
                                    )
                                )
                                Text(
                                    text = "${currentWeather?.temperature ?: "데이터 없음"}°C",
                                    color = Color.Black,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 5.dp, bottom = 2.dp),
                                    style = TextStyle(
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = "${dailyWeather?.get(0)?.minTemp ?: "데이터 없음"}°C / ${
                                        dailyWeather?.get(
                                            0
                                        )?.maxTemp ?: "데이터 없음"
                                    }°C",
                                    color = Color.Black,
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                )
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(
                                color = Color(0xFFE5DD90),
                            )
                    ) {
                        LazyRow(
                            modifier = Modifier
                                .background(
                                    color = Color(0xFFE5DD90),
                                )
                        ) {
                            hourlyWeather?.let {
                                items(count = it.size) { index ->
                                    Card(
                                        modifier = Modifier
                                            .width(70.dp)
                                            .padding(5.dp)
                                            .background(
                                                color = Color(0xFFE5DD90),
                                            ),
                                        elevation = 0.dp,
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(color = Color(0xFFE5DD90)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            val iconName = hourlyWeather?.get(index)?.iconUrl
                                            Column(
                                                modifier = Modifier.background(color = Color.Transparent)
                                            ) {
                                                Text(
                                                    text = "${hourlyWeather?.get(index)?.time}시",
                                                    color = Color(0xFF335C49),
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Normal,
                                                    lineHeight = 24.sp,
                                                    fontFamily = FontFamily.SansSerif
                                                )
                                                Image(
                                                    painter = rememberImagePainter(
                                                        data = "https://openweathermap.org/img/wn/${iconName}@2x.png",
                                                        builder = {
                                                            crossfade(true)
                                                        }
                                                    ),
                                                    contentDescription = "Weather Icon",
                                                    contentScale = ContentScale.Fit
                                                )

                                                Text(
                                                    text = "${hourlyWeather?.get(index)?.temperature}°C",
                                                    color = Color(0xFF335C49),
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Normal,
                                                    lineHeight = 24.sp,
                                                    fontFamily = FontFamily.SansSerif
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .padding(top = 20.dp)
            ) {
                Text(
                    text = "주간예보",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 24.sp,
                    fontFamily = FontFamily.SansSerif
                )
                dailyWeather?.forEach { daily ->
                    DailyItem(
                        weather = daily,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

            }
        }
    }
}


@Composable
fun DailyItem(weather: DailyWeather, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(
                color = Color(0xFFE5DD90),
                shape = RoundedCornerShape(10.dp)
            )
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 4.dp)
                    .background(
                        color = Color(0xFFE5DD90)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = weather.day,
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = weather.date,
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
            Spacer(Modifier.weight(1f))
            val iconName = weather.iconUrl ?: "01d"
            Image(
                painter = rememberImagePainter(
                    data = "https://openweathermap.org/img/wn/${iconName}@2x.png",
                    builder = {
                        crossfade(true)
                    }
                ),
                contentDescription = "Weather Icon",
                modifier = Modifier.size(50.dp)
            )
            Text(
                text = "${weather.minTemp}°C",
                modifier = Modifier.padding(start = 30.dp, end = 20.dp),
                color = Color.Black,
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal
                )
            )
            Text(
                text = "${weather.maxTemp}°C",
                modifier = Modifier.padding(end = 55.dp),
                color = Color.Black,
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal
                )
            )
            Text(
                text = "${weather.humidity}%",
                modifier = Modifier.padding(end = 20.dp),
                color = Color.Black,
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        }

    }
}

fun getWeatherDescription(iconCode: String): String {
    return when (iconCode) {
        "weather_01" -> "맑음"
        "weather_02" -> "구름 조금"
        "weather_03" -> "구름"
        "weather_04" -> "흐림"
        "weather_05" -> "가벼운 비"
        "weather_06" -> "비"
        "weather_07" -> "눈"
        "weather_07" -> "안개"
        else -> "알 수 없음"
    }
}