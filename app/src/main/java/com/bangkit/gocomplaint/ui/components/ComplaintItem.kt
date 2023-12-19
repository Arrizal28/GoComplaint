package com.bangkit.gocomplaint.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material.icons.outlined.Comment
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.bangkit.gocomplaint.data.model.ComplaintResponse
import com.bangkit.gocomplaint.data.model.ComplaintsItem
import com.bangkit.gocomplaint.ui.screen.Loading
import com.bangkit.gocomplaint.ui.theme.poppinsFontFamily
import com.bangkit.gocomplaint.util.calculateTimeDifference
import kotlinx.coroutines.flow.Flow

@Composable
fun ComplaintList(
    modifier: Modifier = Modifier,
    listComplaints: Flow<PagingData<ComplaintsItem>>,
    navigateToDetail: (Int) -> Unit
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
                    //You can add modifier to manage load state when first time response page is loading
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .wrapContentHeight(Alignment.CenterVertically)
                        )
                    }
                }

                loadState.append is LoadState.Loading -> {
                    //You can add modifier to manage load state when next response page is loading
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
                    //You can use modifier to show error message
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
        "N" -> "Open"
        "P" -> "Pending"
        "Y" -> "Complete"
        else -> "Unknown" // Menambahkan ini jika nilai status tidak sesuai dengan yang diharapkan
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
                            fontWeight = FontWeight.Light,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            text = item.createdAt!!.calculateTimeDifference(),
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Light,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Text(
                        text = displayStatus,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Text(
                    text = item.complaint!!, modifier = modifier.padding(bottom = 8.dp),
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = item.location!!, modifier = modifier.padding(bottom = 8.dp),
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
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
                Row(
                    modifier = modifier.padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowUpward,
                        contentDescription = "UpVote",
                        modifier
                            .padding(end = 8.dp)
                            .size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${item.like}",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = modifier.padding(end = 16.dp)
                    )
                    Icon(
                        imageVector = Icons.Outlined.Comment,
                        contentDescription = "Comment",
                        modifier
                            .padding(end = 16.dp)
                            .size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        Divider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

//@Preview
//@Composable
//fun PreviewComplaintItem() {
//    GoComplaintTheme {
//        ComplaintItem()
//    }
//}