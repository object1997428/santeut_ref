package com.santeut.ui.community.course

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.santeut.data.model.response.PostResponse
import com.santeut.ui.community.CommonViewModel
import com.santeut.ui.community.PostViewModel
import com.santeut.ui.community.common.CommentScreen
import com.santeut.ui.community.tips.formatTime
import com.santeut.ui.navigation.top.DefaultTopBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReadPostScreen(
    postId: Int,
    postType: Char,
    postViewModel: PostViewModel,
    commonViewModel: CommonViewModel,
    navController: NavController
) {
    val focusManager = LocalFocusManager.current
    var comment by remember { mutableStateOf("") }
    val post by postViewModel.post.observeAsState()
    val commentList = post?.commentList

    LaunchedEffect(key1 = postId) {
        postViewModel.readPost(postId, postType)
    }

    Scaffold(

        topBar = {
            DefaultTopBar(navController, "커뮤니티")
        },

        bottomBar = {
            if (post != null) {
                com.santeut.ui.guild.CommentSection(postId, postType)
            }
        },
        content = { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                color = MaterialTheme.colorScheme.background
            ) {
                Column {
                    HeaderSection(post)
                    ContentSection(post)
                    commentList?.let {
                        CommentScreen(commentList)
                    }
                }
            }
        }
    )
}


@Composable
fun HeaderSection(post: PostResponse?) {
    post?.let {
        val category = when (post.postType) {
            'T' -> "등산Tip"   // 'T'일 때 "등산Tip"
            'C' -> "등산 코스" // 'C'일 때 "등산 코스"
            else -> "기타"     // 그 외의 경우 "기타"
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = category, style = MaterialTheme.typography.bodyMedium)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(text = post.postTitle, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = "Like",
                    modifier = Modifier.size(24.dp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(text = post.userNickname, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = formatTime(post.createdAt), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun ContentSection(post: PostResponse?) {
    post?.let {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(200.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Text(text = post.postContent, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun CommentSection(
    postId: Int,
    postType: Char,
    comment: String,
    onCommentChange: (String) -> Unit,
    onSend: () -> Unit,
    commonViewModel: CommonViewModel,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .background(Color.White)
    ) {
        fun onSend() {
            commonViewModel.createComment(postId, postType, comment)
        }
        TextField(
            value = comment,
            onValueChange = onCommentChange,
            placeholder = { Text("내용") },
            modifier = Modifier
                .weight(1f)
                .background(Color.Transparent),
            keyboardActions = KeyboardActions(onDone = { onSend() })
        )
        IconButton(onClick = { onSend() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.Send,
                contentDescription = "Send"
            )
        }
    }
}
