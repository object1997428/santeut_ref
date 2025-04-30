package com.santeut.ui.guild

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Send
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.santeut.data.model.response.GuildPostDetailResponse
import com.santeut.ui.community.CommonViewModel
import com.santeut.ui.community.common.CommentScreen
import com.santeut.ui.community.tips.formatTime
import com.santeut.ui.navigation.top.DefaultTopBar

@Composable
fun GuildPostDetailScreen(
    guildPostId: Int,
    navController: NavController,
    guildViewModel: GuildViewModel = hiltViewModel()
) {

    val post by guildViewModel.post.observeAsState()
    val commentList = post?.commentList

    LaunchedEffect(key1 = guildPostId) {
        guildViewModel.getGuildPost(guildPostId)
    }

    Scaffold(

        topBar = {
            DefaultTopBar(navController, "커뮤니티")
        },

        bottomBar = {
            if (post != null) {
                CommentSection(guildPostId)
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
                    post?.let { GuildPostTitle(it) }
                    post?.let { GuildPostContent(it) }
                    commentList?.let {
                        CommentScreen(commentList)
                    }
                }
            }
        }
    )

}

@Composable
fun GuildPostTitle(
    post: GuildPostDetailResponse,
    commonViewModel: CommonViewModel = hiltViewModel()
) {
    val category = when (post.categoryId) {
        0 -> "공지"
        1 -> "자유"
        else -> "기타"
    }

    var isLiked by remember { mutableStateOf(post.like) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = category, style = MaterialTheme.typography.bodyMedium)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(text = post.guildPostTitle, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.weight(1f))

            fun handleLikeClick() {
                if (isLiked) {
                    commonViewModel.cancelLike(post.guildPostId, post.postType)
                } else {
                    commonViewModel.hitLike(post.guildPostId, post.postType)
                }
                isLiked = !isLiked
            }

            Icon(
                imageVector = if (isLiked) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = if (isLiked) "Unlike" else "Like",
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = { handleLikeClick() })
            )
        }
        Text(text = post.userNickName ?: "", style = MaterialTheme.typography.bodyMedium)
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = formatTime(post.createdAt), style = MaterialTheme.typography.bodyMedium)
            Icon(
                imageVector = Icons.Filled.Comment,
                contentDescription = "Comment",
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(text = "${post.commentCnt ?: 0}", style = MaterialTheme.typography.bodyMedium)

            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = "Favorite",
                modifier = Modifier.size(24.dp)

            )
            Spacer(Modifier.width(4.dp))
            Text(text = "${post.likeCnt ?: 0}", style = MaterialTheme.typography.bodyMedium)


        }
    }
}

@Composable
fun GuildPostContent(post: GuildPostDetailResponse) {

    val images = post.images ?: emptyList()

    LazyColumn {

        item {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(200.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Text(text = post.guildPostContent, style = MaterialTheme.typography.bodyLarge)
            }
        }

        if (images.isNotEmpty()) {
            items(images) { image ->
                AsyncImage(model = image, contentDescription = "게시글 이미지")
            }
        }

    }
}

@Composable
fun CommentSection(
    postId: Int,
    postType: Char = 'G',
    commonViewModel: CommonViewModel = hiltViewModel()
) {

    var comment by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {

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
                onValueChange = { comment = it },
                placeholder = { Text("내용") },
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Transparent),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { onSend() })
            )
            IconButton(onClick = { onSend() }) {
                Icon(
                    imageVector = Icons.Outlined.Send,
                    contentDescription = "Send"
                )
            }
        }
    }
}


