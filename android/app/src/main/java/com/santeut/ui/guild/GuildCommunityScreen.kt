package com.santeut.ui.guild

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
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
import com.santeut.data.model.response.GuildPostResponse
import com.santeut.ui.community.tips.formatTime

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GuildCommunityScreen(
    guildId: Int,
    navController: NavController,
    guildViewModel: GuildViewModel = hiltViewModel()
) {
    val postList by guildViewModel.postList.observeAsState(emptyList())
    var selectedCategoryId by remember { mutableStateOf(0) }

    LaunchedEffect(key1 = guildId, key2 = selectedCategoryId) {
        Log.d(
            "GuildScreen",
            "Fetching posts for guildId: $guildId with categoryId $selectedCategoryId"
        )
        guildViewModel.getGuildPostList(guildId, selectedCategoryId)
    }

    Scaffold(
        floatingActionButton = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate("createGuildPost/$guildId")
                    },
                    backgroundColor = Color(0xff678C40),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(40.dp),
                    modifier = Modifier
                        .width(100.dp)
                        .height(45.dp)
                ) {
                    Row(
                        modifier = Modifier
//                        .padding(horizontal = 10.dp, vertical = 1.dp),
                    ) {
                        Row(
                        ) {
                            Icon(imageVector = Icons.Filled.Edit, contentDescription = "글쓰기")
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "글쓰기",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Button(
                    onClick = { selectedCategoryId = 0 },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedCategoryId == 0) Color(0xff678C40) else Color.Transparent,
                        contentColor = if (selectedCategoryId == 1) Color.White else Color(
                            0xff76797D
                        )
                    ),
                    modifier = Modifier.padding(start = 10.dp),
                    border = BorderStroke(1.dp, Color(0xff76797D))
                ) {
                    Text(
                        "공 지",
                        fontSize = 13.sp,
                        color = if (selectedCategoryId == 0) Color.White else Color(0xff76797D)
                    )
                }
                Button(
                    onClick = { selectedCategoryId = 1 },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedCategoryId == 1) Color(0xff678C40) else Color.Transparent,
                        contentColor = if (selectedCategoryId == 1) Color.White else Color(
                            0xff76797D
                        )
                    ),
                    modifier = Modifier.padding(start = 15.dp),
                    border = BorderStroke(1.dp, Color(0xff76797D))
                ) {
                    Text(
                        "자 유",
                        fontSize = 13.sp,
                        color = if (selectedCategoryId == 1) Color.White else Color(0xff76797D)
                    )
                }
            }
            // Content area
            if (postList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .fillMaxHeight()
                ) {
                    androidx.compose.material3.Text(
                        text = "게시글이 없습니다.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .background(color = Color.White)
                        .fillMaxWidth(),
                ) {
                    itemsIndexed(postList.filter { it.categoryId == selectedCategoryId }) { index, post ->
                        if (index == 0) {
                            Spacer(modifier = Modifier.padding(top = 10.dp))
                        }
                        GuildPost(post, navController)
                        Divider(
                            color = Color(0xff76797D),
                            thickness = 1.dp,
                            modifier = Modifier.padding(horizontal = 5.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun GuildPost(post: GuildPostResponse, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clickable(onClick = { navController.navigate("getGuildPost/${post.guildPostId}") })
    ) {
        val dividerWidth = 1.dp;
        val dividerHeight = 12.dp;
        Text(
            text = post.guildPostTitle,
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = post.guildPostContent,
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
                Spacer(Modifier.width(4.dp))
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
                text = formatTime(post.createdAt),
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
                text = post.userNickName,
                style = MaterialTheme.typography.caption,
                color = Color(0xff76797D)
            )
        }
    }
}
