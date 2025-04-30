package com.santeut.ui.community.tips

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.Divider
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.santeut.data.model.response.PostResponse
import com.santeut.ui.community.PostViewModel
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter

@Composable
fun PostTipsScreen(
    navController: NavController,
    postViewModel: PostViewModel = hiltViewModel(),
) {
    var searchText by remember { mutableStateOf("") }

    val posts by postViewModel.posts.observeAsState(initial = emptyList())
    LaunchedEffect(key1 = 'T') {
        postViewModel.getPosts('T')
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top
    ) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navController.navigate("createPost/T")
                    },
                    modifier = Modifier
                        .padding(16.dp),
                    backgroundColor = Color(0xff678C40),
                    contentColor = Color.White,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Write Post",
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.End,
            content = { innerPadding ->
                // 게시글 목록
                LazyColumn(
                    modifier = Modifier
                        .background(color = Color.White)
                        .padding(innerPadding)
                        .fillMaxWidth(),
                ) {
                    itemsIndexed(posts) { index, post ->
                        if (index == 0) {
                            Spacer(modifier = Modifier.padding(top = 10.dp))
                        }
                        TipDetail(post, navController)
                        Divider(
                            color = Color(0xff76797D),
                            thickness = 1.dp,
                            modifier = Modifier.padding(horizontal = 5.dp)
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun TipDetail(post: PostResponse, navController: NavController) {
    val commentList = post
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clickable {
                navController.navigate("readPost/${post.postId}/${post.postType}")
            }
    ) {
        val dividerWidth = 1.dp;
        val dividerHeight = 12.dp;
        Text(
            text = post.postTitle,
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = post.postContent,
            style = MaterialTheme.typography.body1,
            maxLines = 1,
            modifier = Modifier.padding(bottom = 6.dp),
            fontSize = 14.sp
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Comment,
                    contentDescription = "Comment",
                    tint = Color(0xff76797D),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "${post.commentCnt}",
                    style = MaterialTheme.typography.body2,
                    color = Color(0xff76797D)
                )
            }
            Divider(
                color = Color(0xff76797D),
                modifier = Modifier
                    .width(dividerWidth)
                    .height(dividerHeight)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Favorite",
                    tint = Color.Red,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(2.dp))
                Text(
                    text = "${post.likeCnt}",
                    style = MaterialTheme.typography.body2,
                    color = Color.Red
                )
            }
            Divider(
                color = Color(0xff76797D),
                modifier = Modifier
                    .width(dividerWidth)
                    .height(dividerHeight)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.RemoveRedEye,
                    contentDescription = "Favorite",
                    tint = Color(0xff33363F),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(2.dp))
                Text(
                    text = "${post.hitCnt}",
                    style = MaterialTheme.typography.body2,
                    color = Color(0xff76797D)
                )
            }
            Divider(
                color = Color(0xff76797D),
                modifier = Modifier
                    .width(dividerWidth)
                    .height(dividerHeight)
            )
            Text(
                text = post.userNickname,
                style = MaterialTheme.typography.caption,
                color = Color(0xff76797D)
            )
            Divider(
                color = Color(0xff76797D),
                modifier = Modifier
                    .width(dividerWidth)
                    .height(dividerHeight)
            )
            Text(
                text = formatTime(post.createdAt),
                style = MaterialTheme.typography.caption,
                color = Color(0xff76797D)
            )
        }
    }
}

fun formatTime(createdAt: LocalDateTime?): String {

    if (createdAt == null) {
        return "알 수 없음"
    }

    // 현재 시각과의 차이
    val duration = Duration.between(createdAt, LocalDateTime.now()).abs()
    val period = Period.between(createdAt.toLocalDate(), LocalDate.now()).normalized()

    // 하루가 지나면 날짜로 표시
    val oneMinute = Duration.ofMinutes(1)
    val oneHour = Duration.ofHours(1)

    if (duration.compareTo(oneMinute) < 0) {
        return "방금 전"
    } else if (duration.compareTo(oneHour) < 0) {
        val minutes = duration.toMinutes()
        return "$minutes 분 전"
    } else if (period.days < 1) {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return createdAt.format(formatter)
    } else {
        val formatter = DateTimeFormatter.ofPattern("MM월 dd일")
        return createdAt.format(formatter)
    }
}
