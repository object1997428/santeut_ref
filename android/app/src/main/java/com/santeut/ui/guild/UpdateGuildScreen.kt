package com.santeut.ui.guild

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.santeut.R
import com.santeut.data.model.request.CreateGuildRequest
import com.santeut.designsystem.theme.Green

@Composable
fun UpdateGuildScreen(
    navController: NavController,
    guildId: Int,
    guildViewModel: GuildViewModel = hiltViewModel()
) {
    val guild by guildViewModel.guild.observeAsState()

    LaunchedEffect(key1 = guildId) {
        guildViewModel.getGuild(guildId)
    }

    val regions = listOf(
        "전체", "서울", "부산", "대구", "인천", "광주", "대전", "울산", "세종", "경기",
        "충북", "충남", "전북", "전남", "경북", "경남", "제주", "강원", "기타"
    )

    var isDataLoaded by remember { mutableStateOf(false) }
    var guildName by remember { mutableStateOf("") }
    var guildInfo by remember { mutableStateOf("") }
    var guildIsPrivate by remember { mutableStateOf(false) }
    var guildGender by remember { mutableStateOf('A') }
    var regionId by remember { mutableIntStateOf(0) }
    var selectedRegion by remember { mutableStateOf("") }
    var guildMinAge by remember { mutableStateOf("") }
    var guildMaxAge by remember { mutableStateOf("") }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let { newUri ->
                imageUri = newUri
            }
        }
    )

    val context = LocalContext.current
    val multipartImage = createMultiPartBody(imageUri, context)

    LaunchedEffect(key1 = guild) {
        if (guild != null && !isDataLoaded) {
            guildName = guild!!.guildName
            guildInfo = guild!!.guildInfo
            guildIsPrivate = guild!!.guildIsPrivate
            guildGender = guild!!.guildGender
            regionId = guild!!.regionId
            selectedRegion = regions[guild!!.regionId]
            guildMinAge = guild!!.guildMinAge.toString()
            guildMaxAge = guild!!.guildMaxAge.toString()
            isDataLoaded = true
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "동호회 사진",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(2.dp, Green, CircleShape)
                        .clickable {
                            imagePickerLauncher.launch("image/*")
                        }
                )
            } else {
                AsyncImage(
                    model = guild?.guildProfile ?: R.drawable.logo,
                    contentDescription = "동호회 사진",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(2.dp, Green, CircleShape)
                        .clickable {
                            imagePickerLauncher.launch("image/*")
                        }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "이미지 삭제",
                fontSize = 12.sp,
                color = Color.Gray
                // onclick 추가
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = guildName,
                onValueChange = { guildName = it },
                label = { Text("동호회 이름") },
                placeholder = { Text("이름을 입력해주세요(최대 15자)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = guildInfo,
                onValueChange = { guildInfo = it },
                label = { Text("동호회 설명") },
                placeholder = { Text("설명을 입력해주세요") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "공개여부",
                modifier = Modifier.align(Alignment.Start)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = guildIsPrivate == false,
                        onClick = { guildIsPrivate = false },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Green,
                            unselectedColor = Color.LightGray,
                        ),
                    )
                    Text("공개")

                    Spacer(modifier = Modifier.width(8.dp))
                    RadioButton(
                        selected = guildIsPrivate == true,
                        onClick = { guildIsPrivate = true },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Green,
                            unselectedColor = Color.LightGray,
                        ),
                    )
                    Text("비공개")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "성별",
                modifier = Modifier.align(Alignment.Start)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = guildGender == 'A',
                        onClick = { guildGender = 'A' },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Green,
                            unselectedColor = Color.LightGray,
                        ),
                    )
                    Text("성별무관")
                    Spacer(modifier = Modifier.width(8.dp))
                    RadioButton(
                        selected = guildGender == 'M',
                        onClick = { guildGender = 'M' },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Green,
                            unselectedColor = Color.LightGray,
                        ),
                    )
                    Text("남자")
                    Spacer(modifier = Modifier.width(8.dp))
                    RadioButton(
                        selected = guildGender == 'F',
                        onClick = { guildGender = 'F' },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Green,
                            unselectedColor = Color.LightGray,
                        ),
                    )
                    Text("여자")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "지역",
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        ), shape = MaterialTheme.shapes.medium
                    )
                    .clickable {
                        isDropdownExpanded = true
                    }
                    .padding(16.dp)
            ) {
                Text(selectedRegion, modifier = Modifier.align(Alignment.CenterStart))
                DropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .fillMaxHeight(0.5f)
                ) {
                    regions.forEachIndexed { index, region ->
                        DropdownMenuItem(
                            text = { Text(text = region) },
                            onClick = {
                                selectedRegion = region
                                regionId = index
                                isDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    value = guildMinAge,
                    onValueChange = { guildMinAge = it },
                    label = { Text("최소 연령") },
                    placeholder = { Text(text = "10") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        autoCorrect = true
                    ),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("~")
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = guildMaxAge,
                    onValueChange = { guildMaxAge = it },
                    label = { Text("최대 연령") },
                    placeholder = { Text(text = "100") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        autoCorrect = true
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        val isInputValid = guildName.isNotEmpty() &&
                guildInfo.isNotEmpty()

        Button(
            onClick = {
                guildViewModel.updateGuild(
                    guildId, multipartImage, CreateGuildRequest(
                        guildName,
                        guildInfo,
                        guildIsPrivate,
                        regionId,
                        guildGender,
                        guildMinAge.toIntOrNull() ?: 0,
                        guildMaxAge.toIntOrNull() ?: 100
                    )
                )
                navController.navigate("guild")
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Green,
                disabledContainerColor = Color.LightGray,
            ),
            enabled = isInputValid,
        ) {
            if (isInputValid) {
                Text(
                    text = "완료",
                    color = Color.White
                )
            } else {
                Text(
                    text = "완료",
                    color = Color.Black
                )
            }
        }
    }
}
