package com.santeut.ui.mountain

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.santeut.R
import com.santeut.data.model.response.MountainResponse
import com.santeut.designsystem.theme.Green
import com.santeut.ui.home.SearchMountainBar
import com.santeut.ui.party.SelectedMountainCard

@Composable
fun MountainListScreen(
    guildId: Int?,
    type: String?,
    name: String, region: String?,
    navController: NavController,
    mountainViewModel: MountainViewModel = hiltViewModel()
) {
    val mountains by mountainViewModel.mountains.observeAsState(emptyList())

    LaunchedEffect(key1 = name) {
        mountainViewModel.searchMountain(name, region)
    }

    Column {
        if (type != "create") {
            SearchMountainBar(
                null,
                null,
                navController
            )

            LazyColumn {
                items(mountains) { mountain ->
                    MountainCard(navController, mountain)
                }
            }
        } else {
            SearchMountainBar(
                guildId,
                "create",
                navController
            )

            LazyColumn {
                items(mountains) { mountain ->
                    SelectedMountainCard(guildId, navController, mountain)
                }
            }
        }
    }
}

@Composable
fun MountainCard(navController: NavController, mountain: MountainResponse) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    )
    {
        Card(
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .padding(16.dp)
                .clickable(onClick = { navController.navigate("mountain/${mountain.mountainId}") }),
            elevation = CardDefaults.cardElevation(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                AsyncImage(
                    model = mountain.image ?: R.drawable.logo,
                    contentDescription = "산 사진",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Crop
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
                ) {
                    Text(
                        text = mountain.mountainName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(10.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${mountain.height}m",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = mountain.regionName,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 18.dp, top = 0.dp, bottom = 16.dp),
                ) {
                    Text(
                        text = "${mountain.courseCount}개 코스",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    if (mountain.isTop100) {
                        Box(
                            modifier = Modifier
                                .background(color = Green, shape = RoundedCornerShape(10.dp))
                                .padding(vertical = 4.dp, horizontal = 8.dp)
                        ) {
                            Text(
                                text = "100대 명산",
                                color = Color.White,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
