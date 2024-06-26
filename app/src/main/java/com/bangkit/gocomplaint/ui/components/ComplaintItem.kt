package com.bangkit.gocomplaint.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.bangkit.gocomplaint.R
import com.bangkit.gocomplaint.data.model.ComplaintsItem
import com.bangkit.gocomplaint.ui.screen.ErrorScreen
import com.bangkit.gocomplaint.ui.theme.poppinsFontFamily
import com.bangkit.gocomplaint.util.calculateTimeDifference
import kotlinx.coroutines.flow.Flow

@Composable
fun ComplaintList(
    modifier: Modifier = Modifier,
    listComplaints: Flow<PagingData<ComplaintsItem>>,
    navigateToDetail: (Int) -> Unit,
) {
    val complaintListItems: LazyPagingItems<ComplaintsItem> = listComplaints.collectAsLazyPagingItems()

    LazyColumn {
        items(complaintListItems.itemCount){ item ->
            ComplaintItem(item = complaintListItems[item]!!,
                modifier = Modifier.clickable{
                    navigateToDetail(
                        complaintListItems[item]?.id ?: -1
                    )
                })
        }
        complaintListItems.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentHeight(Alignment.CenterVertically)
                                .wrapContentWidth(Alignment.CenterHorizontally)
                                .size(30.dp)
                        )
                    }
                }

                loadState.append is LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.Center)
                            )
                        }
                    }
                }

                loadState.append is LoadState.Error -> {
                    item {
                        ErrorScreen(retryAction = { refresh() })
                    }
                }
            }
        }
    }
}


@Composable
fun ComplaintItem(
    modifier: Modifier = Modifier,
    item: ComplaintsItem,
) {
    val displayStatus = when (item.status) {
        "O" -> "Open"
        "P" -> "Pending"
        "Y" -> "Complete"
        "N" -> "Closed"
        else -> "Unknown"
    }

    val colorStatus = when (item.status) {
        "O" -> Color(0xFF6C9BCF)
        "P" -> Color(0xFFF7C52E)
        "Y" -> Color(0xFF1B9C85)
        "N" -> Color(0xFFFF0060)
        else -> Color(0xFF000000)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.tertiary)
        ) {
            Image(
                painter = painterResource(R.drawable.default_profile_icon_24),
                contentDescription = "profile",
                modifier = modifier
                    .padding(start = 16.dp, top = 16.dp)
                    .size(34.dp)
                    .shadow(4.dp, CircleShape, clip = false)
                    .clip(CircleShape)
            )
            Column(
                modifier = modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Column {
                        Text(
                            text = item.username!!,
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Row(
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = item.createdAt!!.calculateTimeDifference(LocalContext.current),
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Light,
                                fontSize = 12.sp,
                                color = Color.Gray,
                                modifier = modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = item.location!!, modifier = modifier.padding(bottom = 8.dp),
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Light,
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                    Text(
                        text = displayStatus,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = colorStatus
                    )
                }
                Text(
                    text = item.complaint!!, modifier = modifier.padding(bottom = 8.dp),
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                if (item.file != null) {
                    AsyncImage(
                        model = "${item.file}",
                        contentDescription = "image complaint",
                        contentScale = ContentScale.Crop,
                        modifier = modifier
                            .padding(bottom = 8.dp)
                            .size(width = 288.dp, height = 155.dp)
                            .shadow(8.dp, RoundedCornerShape(12.dp), clip = false)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
            }
        }
    }
}