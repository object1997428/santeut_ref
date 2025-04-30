package com.santeut.ui.community.tips

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.santeut.data.model.request.CreatePostRequest
import com.santeut.ui.community.PostViewModel
import com.santeut.ui.navigation.top.CreateTopBar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

@Composable
fun CreateTipPostScreen(
    navController: NavHostController,
    postViewModel: PostViewModel,
    postType: Char
) {

    val context = LocalContext.current

    val focusManager = LocalFocusManager.current
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    var imageUris by remember { mutableStateOf(listOf<Uri>()) }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let { newUri ->
                imageUris = imageUris + newUri
            }
        }
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    imagePickerLauncher.launch("image/*")
                },
            ) {
                Row {
                    Icon(Icons.Outlined.Photo, contentDescription = "사진 추가")
                    Text(text = "사진 추가")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) {
        val multipartImages = createMultiPartBody(imageUris, context)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            CreateTopBar(
                navController,
                "글쓰기",
                onWriteClick = {
                    Log.d("Screen", "클릭 성공")
                    postViewModel.createPost(
                        multipartImages,
                        CreatePostRequest(
                            title, content, postType, 0
                        )
                    )
                    navController.navigate("postTips")
                }
            )

            TextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text("제목을 작성해주세요") },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .border(1.dp, Color(0XFF678C40)),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                value = content,
                onValueChange = { content = it },
                placeholder = { Text("내용을 작성해주세요") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(Color.White)
                    .border(1.dp, Color(0XFF678C40)),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )
            Spacer(modifier = Modifier.height(16.dp))
            ImagePreviewSection(images = imageUris)
        }
    }
}

@Composable
fun ImagePreviewSection(images: List<Uri>) {
    LazyRow {
        items(images) { imageUri ->
            Image(
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = "Uploaded Image",
                modifier = Modifier
                    .size(100.dp)
                    .padding(4.dp)
            )
        }
    }
}

private fun createMultiPartBody(uriList: List<Uri>?, context: Context): List<MultipartBody.Part>? {
    val multipartList = mutableListOf<MultipartBody.Part>()
    if (uriList != null) {
        for (uri in uriList) {
            try {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val mimeType =
                        context.contentResolver.getType(uri) ?: "application/octet-stream"
                    val fileName = uri.lastPathSegment ?: "upload.file"
                    val byteArray = inputStream.readBytes()
                    val requestBody = byteArray.toRequestBody(mimeType.toMediaTypeOrNull())
                    val part = MultipartBody.Part.createFormData("images", fileName, requestBody)
                    multipartList.add(part)
                }
            } catch (e: Exception) {
                Log.e("MultiPart", "Error processing file Uri: $uri", e)
            }
        }
        return multipartList
    }
    return null
}