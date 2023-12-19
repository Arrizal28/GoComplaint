package com.bangkit.gocomplaint.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bangkit.gocomplaint.R
import com.bangkit.gocomplaint.data.model.ComplaintResponse
import com.bangkit.gocomplaint.data.model.ComplaintsItem
import com.bangkit.gocomplaint.model.FakeDataComplaint
import com.bangkit.gocomplaint.model.FakeDataProfile
import com.bangkit.gocomplaint.ui.theme.GoComplaintTheme
import com.bangkit.gocomplaint.ui.theme.poppinsFontFamily
import com.bangkit.gocomplaint.util.calculateTimeDifference
import okhttp3.internal.userAgent

@Composable
fun ProfileList(
    modifier: Modifier = Modifier,
    item: ComplaintResponse
) {
    LazyColumn {
        items(count = item.complaints.size, itemContent = {index ->
            ProfileItem(
                item = item.complaints[index]
            )
        })
    }
}

@Composable
fun ProfileItem(
    modifier: Modifier = Modifier,
    item: ComplaintsItem
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.tertiary)
                .padding(16.dp)
        ) {
            if (item.file != null) {
                AsyncImage(model = item.file, contentDescription = "image", contentScale = ContentScale.Crop ,modifier = modifier
                    .size(54.dp)
                    .weight(1f)
                    .padding(end = 8.dp)
                    .clip(RoundedCornerShape(8.dp)))
            }
            Column(
                modifier = modifier
                    .padding(end = 4.dp)
                    .weight(3f),
            ) {
                Text(
                    text = item.complaint!!,
                    maxLines = 2,
                    lineHeight = 12.sp,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
                Text(
                    text = item.createdAt!!.calculateTimeDifference(),
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = modifier
                        .padding(end = 8.dp)
                )
            }
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.primary,
                modifier = modifier
                    .width(24.dp)
                    .height(24.dp)
            )
        }
        Divider(
            color = Color.Gray,
            thickness = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun ProfileItemPreview() {
//    GoComplaintTheme {
//        ProfileItem()
//    }
//}