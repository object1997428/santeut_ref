package com.santeut.ui.guild

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.santeut.R
import com.santeut.data.model.request.CreateGuildRequest
import com.santeut.designsystem.theme.Green
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody


@Composable
fun CreateGuildScreen(
    navController: NavController,
    guildViewModel: GuildViewModel = hiltViewModel()
) {
    var guildName by remember { mutableStateOf("") }
    var guildInfo by remember { mutableStateOf("") }
    var guildIsPrivate by remember { mutableStateOf(false) }
    var guildGender by remember { mutableStateOf('A') }
    var regionId by remember { mutableIntStateOf(0) }
    var selectedRegion by remember { mutableStateOf("전체") }
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

    val regions = listOf(
        "전체", "서울", "부산", "대구", "인천", "광주", "대전", "울산", "세종", "경기",
        "충북", "충남", "전북", "전남", "경북", "경남", "제주", "강원", "기타"
    )
    val context = LocalContext.current
    val multipartImage = createMultiPartBody(imageUri, context)

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

            // 사진 선택
            if (imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(model = imageUri),
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
                val image: Painter = painterResource(id = R.drawable.logo)
                Image(
                    painter = image,
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
            )
            // 사진 선택 끝

            // 동호회 이름, 설명 입력
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
            // 동호회 이름, 설명 입력 끝

            Spacer(modifier = Modifier.height(16.dp))

            // 동호회 공개 여부
            Text(
                text = "공개여부",
                modifier = Modifier.align(Alignment.Start)
            )
//            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = guildIsPrivate == false,
                        onClick = {
                            guildIsPrivate = false
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Green,
                            unselectedColor = Color.LightGray,
                        ),
                    )
                    Text("공개")

                    Spacer(modifier = Modifier.width(8.dp))
                    RadioButton(
                        selected = guildIsPrivate == true,
                        onClick = {
                            guildIsPrivate = true
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Green,
                            unselectedColor = Color.LightGray,
                        ),
                    )
                    Text("비공개")
                }
            }
            // 공개여부 끝

            Spacer(modifier = Modifier.height(8.dp))

            // 성별
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
            // 성별 끝

            Spacer(modifier = Modifier.height(16.dp))

            // 지역
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
            // 지역 끝

            Spacer(modifier = Modifier.height(16.dp))

            // 연령
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
            // 연령 끝

        }

        // 완료 버튼
        val isInputValid = guildName.isNotEmpty() &&
                guildInfo.isNotEmpty()

        Button(
            onClick = {
                guildViewModel.createGuild(
                    multipartImage, CreateGuildRequest(
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

fun createMultiPartBody(uri: Uri?, context: Context): MultipartBody.Part? {
    var guildProfile: MultipartBody.Part? = null
    if (uri != null) {
        try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val mimeType = context.contentResolver.getType(uri) ?: "application/octet-stream"
                val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType) ?: ""
                val fileName = uri.lastPathSegment?.let {
                    if (extension.isNotEmpty()) {
                        "$it.$extension"
                    } else {
                        it
                    }
                } ?: "upload.file"
                val byteArray = inputStream.readBytes()
                val requestBody = byteArray.toRequestBody(mimeType.toMediaTypeOrNull())
                guildProfile =
                    MultipartBody.Part.createFormData("guildProfile", fileName, requestBody)
            }
        } catch (e: Exception) {
            Log.e("MultiPart", "Error processing file Uri: $uri", e)
        }
    }
    return guildProfile
}
