package com.santeut.ui.mypage

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.santeut.ui.party.PartyViewModel
import java.time.LocalDate
import java.time.YearMonth
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.santeut.R

data class CalendarDate(
    val day: Int,
    val month: Int,
    val year: Int
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MyScheduleScreen(
    partyViewModel: PartyViewModel = hiltViewModel(),
) {
    //등산기록 목록
    val myScheduleList by partyViewModel.myScheduleList.observeAsState(emptyList())
    val activeDates = myScheduleList.map { LocalDate.parse(it) }

    //나타내는 년, 월
    var currentYear by remember { mutableStateOf(LocalDate.now().year) }
    var currentMonth by remember { mutableStateOf(LocalDate.now().monthValue) }
    val weekList = listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa")

    LaunchedEffect(key1 = currentYear, key2 = currentMonth) {
        partyViewModel.getMyScheduleList(currentYear, currentMonth)
    }

    fun daysInMonth(month: Int, year: Int): Int {
        return when (month) {
            2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
            4, 6, 9, 11 -> 30
            else -> 31
        }
    }

    //현재 달의 첫 날짜 예시: 6월 -> 6월 1일
    val firstDayOfMonth = LocalDate.of(currentYear, currentMonth, 1)
    //6월 1일의 요일 받아옴 -> dayOfWeek=3이면 6월 1일은 수요일
    val dayOfWeek = firstDayOfMonth.dayOfWeek.value
    //6월의 총 날짜수
    val daysInCurrentMonth = daysInMonth(currentMonth, currentYear)

    Log.d("firstDayOfMonth", "MyScheduleScreen: $firstDayOfMonth")
    Log.d("dayOfWeek", "MyScheduleScreen: $dayOfWeek")
    Log.d("daysInCurrentMonth", "MyScheduleScreen: $daysInCurrentMonth")

    val dates: List<CalendarDate> = (1..daysInCurrentMonth).map { day ->
        CalendarDate(day, currentMonth, currentYear)
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.padding(
            horizontal = 35.dp,
            vertical = 15.dp)) {
            Column(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color(0xFFE1E1E1),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(
                        top = 4.dp,
                        start = 6.dp,
                        end=6.dp
                    )
            ) {
                // < 2024년 5월 >
                Row(
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 8.dp
                        )
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "keyboardArrowLeft",
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .clickable {
                                Log.i("keyboardArrowLeft", "MyScheduleScreen: 달 내려감")
                                if (currentMonth - 1 < 1) {
                                    currentYear--
                                    currentMonth = 12
                                } else currentMonth--
                            }
                    )
                    Text(
                        text = "${currentYear}년 ${currentMonth}월",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            lineHeight = 32.sp,
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp),
                        textAlign = TextAlign.Center,
                    )
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "keyboardArrowRight",
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .clickable {
                                Log.i("keyboardArrowRight", "MyScheduleScreen: 달 올라감")
                                if (currentMonth + 1 > 12) {
                                    currentYear++
                                    currentMonth = 1
                                } else currentMonth++
                            }
                    )
                }
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White)
                ) {
                    // Su Mo Tu We Th Fr Sa
                    FlowRow(
                        modifier = Modifier
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                                top = 12.dp
                            )
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        weekList.forEach { day ->
                            Text(
                                text = day,
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    lineHeight = 22.sp,
                                ),
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    // DateItem
                    FlowRow(
                        modifier = Modifier
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 12.dp,
                                top = 10.dp
                            )
                            .fillMaxWidth(),
                        maxItemsInEachRow = 7,
                    ) {
                        var select by remember { mutableStateOf("") }
                        for (i in 1..dayOfWeek) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                        dates.forEach { date ->
                            Column(
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .weight(1f)
                                    .clickable {
                                        select = "${date.year}${date.month}${date.day}"
                                    },
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                val dateLocal = LocalDate.of(date.year, date.month, date.day)

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(40))
                                        .background(
                                            if (LocalDate
                                                    .now()
                                                    .isEqual(
                                                        LocalDate.of(
                                                            date.year,
                                                            date.month,
                                                            date.day
                                                        )
                                                    )
                                            )
                                                Color(0xFFE5DD90)
                                            else
                                                Color.Transparent,
                                        )
                                ) {
                                    Text(
                                        text = (date.day).toString(),
                                        style = TextStyle(
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 17.sp,
                                            lineHeight = 22.sp,
                                        ),
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        color = if (
                                            LocalDate.now().dayOfMonth == date.day &&
                                            LocalDate.now().monthValue == date.month &&
                                            LocalDate.now().year == date.year
                                        ) Color.White else Color.Black,
                                    )
                                }
                                if (activeDates.contains(dateLocal)) {
                                    Icon(
                                        Icons.Filled.Circle,
                                        contentDescription = "Active Event",
                                        modifier = Modifier.size(20.dp),
                                        tint = Color(0xFF678C40)
                                    )
                                }
                                else {
                                    Spacer(Modifier.size(20.dp))
                                }
                            }
                        }
                        for (i in 1..7) Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}
