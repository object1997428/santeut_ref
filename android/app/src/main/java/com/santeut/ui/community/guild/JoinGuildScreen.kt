package com.santeut.ui.community.guild

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FilterListOff
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.santeut.R
import com.santeut.data.model.response.GuildResponse
import com.santeut.designsystem.theme.DarkGreen
import com.santeut.designsystem.theme.Green
import com.santeut.ui.guild.GuildViewModel
import com.santeut.ui.guild.genderToString
import com.santeut.ui.guild.regionName


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun JoinGuildScreen(
    guildViewModel: GuildViewModel = hiltViewModel(),
    guildId: Int,
    onClearData: () -> Unit
) {
    val guildList by guildViewModel.guilds.observeAsState(initial = emptyList())

    // 필터 bottom sheet
    var showBottomFilterSheet by remember { mutableStateOf(false) }
    val filterSheetState = rememberModalBottomSheetState()
    var searchFilterRegion by remember { mutableStateOf("") }
    var searchFilterGender by remember { mutableStateOf("") }
    // 필터 적용 되었는지?
    var isFiltered by remember { mutableStateOf(false) }

    // 검색어
    var searchWord by remember { mutableStateOf("") }


    LaunchedEffect(key1 = null) {
        guildViewModel.getGuilds()
    }

    Scaffold(
        topBar = {
            GuildSearchBar(
                guildViewModel,
                searchWord,
                onSearchTextChanged = { searchWord = it },
                onClickFilter = {
                    showBottomFilterSheet = true
                },
                isFiltered = isFiltered,
            )
        }, content = { paddingValues ->

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                if (guildList.isEmpty()) {
                    Text(
                        text = "동호회가 없습니다",
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(alignment = Alignment.CenterHorizontally),
                        horizontalAlignment = Alignment.Start
                    ) {
                        items(guildList) { guild ->
                            GuildCard(guild, guildViewModel, guildId, onClearData)
                        }
                    }
                }
            }

            // 검색 필터
            val context = LocalContext.current
            if (showBottomFilterSheet) {
                ModalBottomSheet(
                    modifier = Modifier
                        .fillMaxHeight(.8f),
                    onDismissRequest = {
                        showBottomFilterSheet = false
                        searchFilterGender = ""
                        searchFilterRegion = ""
                    },
                    sheetState = filterSheetState,
                ) {
                    Surface(modifier = Modifier.padding(16.dp)) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp, 0.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "필터 검색",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(
                                        0.dp, 8.dp
                                    )
                                )
                                Text(
                                    text = "초기화",
                                    color = Color.DarkGray,
                                    fontSize = 14.sp,
                                    modifier = Modifier.clickable {
                                        searchFilterRegion = ""
                                        searchFilterGender = ""
                                    }
                                )
                            }
                            // 지역 필터
                            Text(
                                text = "지역",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(0.dp, 8.dp, 0.dp, 8.dp)
                            )
                            CustomRadioGroup(
                                6,
                                3,
                                region,
                                selectedOption = searchFilterRegion,
                                onSelectionChange = { searchFilterRegion = it })
                            // 성별 필터
                            Text(
                                text = "성별",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(0.dp, 8.dp, 0.dp, 8.dp)
                            )
                            CustomRadioGroup(
                                1,
                                3,
                                gender,
                                selectedOption = searchFilterGender,
                                onSelectionChange = { searchFilterGender = it })
                            // 적용 버튼
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(0.dp, 16.dp, 0.dp, 0.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Green,
                                    contentColor = Color.White
                                ),
                                onClick = {
                                    if (searchFilterRegion == "" && searchFilterGender == "") {
                                        guildViewModel.getGuilds()
                                        showBottomFilterSheet = false
                                        isFiltered = false
                                    } else if (searchFilterRegion == "") {
                                        Toast.makeText(context, "지역을 선택해주세요", Toast.LENGTH_SHORT)
                                            .show()
                                        isFiltered = false
                                    } else if (searchFilterGender == "") {
                                        Toast.makeText(context, "성별을 선택해주세요", Toast.LENGTH_SHORT)
                                            .show()
                                        isFiltered = false
                                    } else {
                                        guildViewModel.searchGuilds(
                                            searchFilterRegion,
                                            searchFilterGender
                                        )
                                        showBottomFilterSheet = false
                                        isFiltered = true
                                        Log.d("동호회 검색) 지역", searchFilterRegion)
                                        Log.d("동호회 검색) 성별", searchFilterGender)
                                    }
                                }
                            ) {
                                Text(
                                    text = "적용하기",
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    )
    // 검색 필터
    val context = LocalContext.current
    if (showBottomFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomFilterSheet = false
                searchFilterGender = ""
                searchFilterRegion = ""
            },
            sheetState = filterSheetState,
        ) {
            Surface(modifier = Modifier.padding(16.dp)) {
                Column {
                    // 지역 필터
                    Text(
                        text = "지역",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(4.dp, 0.dp, 0.dp, 0.dp)
                    )
                    CustomRadioGroup(
                        6,
                        3,
                        region,
                        selectedOption = searchFilterRegion,
                        onSelectionChange = { searchFilterRegion = it })
                    // 성별 필터
                    Text(
                        text = "성별",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(4.dp, 0.dp, 0.dp, 0.dp)
                    )
                    CustomRadioGroup(
                        1,
                        3,
                        gender,
                        selectedOption = searchFilterGender,
                        onSelectionChange = { searchFilterGender = it })
                    // 적용 버튼
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 16.dp, 0.dp, 0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Green,
                            contentColor = Color.White
                        ),
                        onClick = {
                            if (searchFilterRegion == "") {
                                Toast.makeText(context, "지역을 선택해주세요", Toast.LENGTH_SHORT).show()
                            } else if (searchFilterGender == "") {
                                Toast.makeText(context, "성별을 선택해주세요", Toast.LENGTH_SHORT).show()
                            } else {
                                guildViewModel.searchGuilds(searchFilterRegion, searchFilterGender)
                                showBottomFilterSheet = false
                                Log.d("동호회 검색) 지역", searchFilterRegion)
                                Log.d("동호회 검색) 성별", searchFilterGender)
                            }
                        }
                    ) {
                        Text(
                            text = "적용하기",
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun GuildCard(
    guild: GuildResponse,
    guildViewModel: GuildViewModel,
    guildId: Int,
    onClearData: () -> Unit
) {

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(guildId) {
        showBottomSheet = (guild.guildId == guildId)
    }
    Card(
        modifier = Modifier
            .clickable(onClick = { showBottomSheet = true })
            .fillMaxWidth()
            .height(100.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 8.dp,

        ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = guild.guildProfile ?: R.drawable.logo,
                contentDescription = "동호회 사진",
                modifier = Modifier
                    .size(100.dp),
                contentScale = ContentScale.Crop
            )

            Column {
                Spacer(modifier = Modifier.width(10.dp))
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .weight(1f),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = guild.guildName,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 5.dp, top = 5.dp)
                    )
                    {
                        Icon(
                            imageVector = Icons.Filled.PersonOutline,
                            contentDescription = "회원수",
                            modifier = Modifier.size(20.dp),
                            tint = Color(0xff76797D),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        androidx.compose.material3.Text(
                            text = "${guild.guildMember ?: 0} 명",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xff76797D)
                        )
                    }


                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PinDrop,
                            contentDescription = "지역",
                            modifier = Modifier.size(20.dp),
                            tint = Color(0xff76797D)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        androidx.compose.material3.Text(
                            text = regionName(guild.regionId),
                            color = Color(0xff76797D),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }


                }
            }



            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                        if (guildId != 0 && onClearData != {})
                            onClearData()
                    },
                    sheetState = sheetState
                ) {
                    Surface {
                        Column {
                            GuildDetail(guild, guildViewModel)
                        }
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }

}

@Composable
fun GuildDetail(guild: GuildResponse, guildViewModel: GuildViewModel) {
    var isRequested by remember { mutableStateOf(guild.joinStatus != 'N') }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AsyncImage(
            model = guild.guildProfile ?: R.drawable.logo,
            contentDescription = "동호회 사진",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = guild.guildName,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = guild.guildInfo,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "인원 ${guild.guildMember}명",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "성별 ${genderToString(guild)}",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "연령 ${guild.guildMinAge}세 ~ ${guild.guildMaxAge}세",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Button(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(0.dp, 16.dp, 0.dp, 0.dp),
            onClick = {
                if (guild.joinStatus == 'N') {
                    guildViewModel.applyGuild(guild.guildId)
                    isRequested = true
                }
            },
            enabled = !isRequested,
            colors = ButtonDefaults.buttonColors(
                containerColor = Green,
                contentColor = Color.White,
                disabledContainerColor = Color.LightGray,
                disabledContentColor = Color.Black,
            )
        ) {
            if (guild.joinStatus === 'N') {
                Text(
                    text = "가입 신청 하기",
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            } else if (guild.joinStatus == 'R') {
                Text(
                    text = "가입 신청 완료",
                    fontWeight = FontWeight.SemiBold
                )
            } else {
                Text(
                    text = "이미 가입한 동호회",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }

}

@Composable
fun GuildSearchBar(
    guildViewModel: GuildViewModel,
    enteredText: String,
    onSearchTextChanged: (String) -> Unit,
    onClickFilter: () -> Unit,
    isFiltered: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(top = 8.dp, bottom = 8.dp),
            textStyle = TextStyle(fontSize = 12.sp, color = Color(0xff666E7A)),
            value = enteredText,
            onValueChange = { text ->
                onSearchTextChanged(text)
            },
            placeholder = {
                androidx.compose.material.Text(
                    text = "어느 동호회를 찾으시나요?",
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
                            Log.d("동호회 검색 버튼 클릭", enteredText)
                            guildViewModel.searchGuildByName(enteredText)
                        }
                )
            }
        )
        Spacer(modifier = Modifier.width(15.dp))
        IconButton(
            onClick = {
                onClickFilter()
            }
        ) {
            if (!isFiltered) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.FilterListOff,
                    contentDescription = "필터",
                    tint = Color.DarkGray,
                    modifier = Modifier.size(30.dp),
                )
            } else {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "필터",
                    tint = Green,
                    modifier = Modifier.size(30.dp),
                )
            }
        }
    }
}

@Composable
fun CustomRadioGroup(
    row: Int,
    col: Int,
    options: List<String>,
    selectedOption: String,
    onSelectionChange: (String) -> Unit
) {

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        for (i in 0 until row) {
            Row(
                modifier = Modifier
                    .padding(
                        all = 4.dp,
                    )
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (j in 0 until col) {
                    val optionIndex = i * col + j
                    if (optionIndex < options.size) {
                        val text = options[optionIndex]
                        Text(
                            text = text,//text,
                            style = typography.body1.merge(),
                            textAlign = TextAlign.Center,
                            color = if (text == selectedOption) {
                                Color.White
                            } else {
                                DarkGreen
                            },
                            modifier = Modifier
                                .clip(
                                    shape = RoundedCornerShape(
                                        size = 12.dp,
                                    ),
                                )
                                .clickable {
                                    onSelectionChange(text)
                                }
                                .background(
                                    if (text == selectedOption) {
                                        Green
                                    } else {
                                        Color.Transparent
                                    }
                                )
                                .border(
                                    width = 1.dp,
                                    color = Green,
                                    shape = RoundedCornerShape(
                                        size = 12.dp,
                                    ),
                                )
                                .padding(
                                    vertical = 12.dp,
                                    horizontal = 16.dp,
                                )
                                .weight(1f),
                        )
                    }
                }
            }
        }
    }
}

val gender = listOf(
    "성별무관",
    "남성",
    "여성"
)

val region = listOf(
    "전체",
    "서울",
    "부산",
    "대구",
    "인천",
    "광주",
    "대전",
    "울산",
    "세종",
    "경기",
    "충북",
    "충남",
    "전북",
    "전남",
    "경북",
    "경남",
    "제주",
    "강원"
)
