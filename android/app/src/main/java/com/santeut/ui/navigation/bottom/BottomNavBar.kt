package com.santeut.ui.navigation.bottom

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.santeut.R
import com.santeut.designsystem.theme.SanteutTheme


@Preview
@Composable
fun BottomNavBarPreview(
) {
    SanteutTheme {
        BottomNavBar(
            currentTap = "Home",
            onTabClick = {},
        )
    }
}

@Composable
fun BottomNavBar(
    currentTap: String?,
    onTabClick: (BottomTap) -> Unit
) {
    val NoBottomScreen = listOf(
        "landing",
        "login",
        "signup",
        "noti",
        "chatRoom/{partyId}",
        "getGuildPost/{guildPostId}",
        "searchPlant",
        "map"
    )

    AnimatedVisibility(
        modifier = Modifier
            .background(color = Color(0xffFEFEFE))
            .padding(8.dp),
        visible = currentTap != null && !NoBottomScreen.contains(currentTap),
        enter = fadeIn() + slideIn { IntOffset(it.width, 0) },
        exit = fadeOut() + slideOut { IntOffset(it.width, 0) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(12.dp, 0.dp, 12.dp, 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BottomTap.entries.forEach { tab ->
                BottomBarItem(
                    tab = tab,
                    selected = currentTap == tab.description,
                    onClick = { onTabClick(tab) }
                )
            }
        }
    }
}

@Composable
fun RowScope.BottomBarItem(
    tab: BottomTap,
    selected: Boolean,
    onClick: () -> Unit
) {
    if (tab.title != "지도") {
        Box(
            modifier = Modifier
                .width(60.dp)
                .selectable(
                    selected = selected,
                    onClick = onClick
                ),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Icon(
                    imageVector = tab.icon,
                    contentDescription = tab.description,
                    tint = if (selected) Color(0xff678C40) else Color(0xff76797D)
                )
                Spacer(
                    modifier = Modifier
                        .height(4.dp)
                )
                Text(
                    text = tab.title,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (selected) Color(0xff678C40) else Color(0xff76797D)
                )
            }
        }
    } else {
        Box(
            modifier = Modifier
                .width(80.dp)
                .selectable(
                    selected = selected,
                    onClick = onClick,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.map_bottom_tap),
                contentDescription = "map_bottom_tap",
                contentScale = ContentScale.Crop,

                modifier = Modifier
                    .fillMaxHeight()
            )
        }
    }

}

enum class BottomTap(
    val icon: ImageVector,
    val route: String,
    val title: String,
    val description: String
) {
    HOME(
        icon = Icons.Outlined.Home,
        route = "home_graph",
        title = "홈",
        description = "home"
    ),
    COMMUNITY(
        icon = Icons.Outlined.Groups,
        route = "community_graph",
        title = "커뮤니티",
        description = "community"
    ),
    MAP(
        icon = Icons.Default.LocationOn,
        route = "map_graph",
        title = "지도",
        description = "map"
    ),
    GUILD(
        icon = Icons.Outlined.AccountBalance,
        route = "guild_graph",
        title = "동호회",
        description = "guild"
    ),
    MYPAGE(
        icon = Icons.Outlined.Person,
        route = "mypage_graph",
        title = "마이페이지",
        description = "mypage"
    )
}