package com.bangkit.gocomplaint.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bangkit.gocomplaint.R
import com.bangkit.gocomplaint.data.model.ComplaintResponse
import com.bangkit.gocomplaint.data.model.ComplaintsItem
import com.bangkit.gocomplaint.ui.theme.poppinsFontFamily
import com.bangkit.gocomplaint.util.calculateTimeDifference

@Composable
fun ProfileList(
    modifier: Modifier = Modifier,
    item: ComplaintResponse,
    navigateToDetail: (Int) -> Unit,
    deleteClick: (complaintId: String) -> Unit,
) {
    LazyColumn {
        items(count = item.complaints.size, itemContent = { index ->
            ProfileItem(
                item = item.complaints[index],
                modifier = Modifier.clickable {
                    navigateToDetail(
                        item.complaints[index].id!!
                    )
                },
                complaintId = item.complaints[index].id!!.toString(),
                deleteClick = { deleteClick(it) }
            )
        })
    }
}

@Composable
fun ProfileItem(
    modifier: Modifier = Modifier,
    item: ComplaintsItem,
    complaintId: String,
    deleteClick: (complaintId: String) -> Unit
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

    val deleteDialog = remember { mutableStateOf(false) }

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
                AsyncImage(
                    model = item.file,
                    contentDescription = "image",
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .size(54.dp)
                        .weight(1f)
                        .padding(end = 8.dp)
                        .shadow(8.dp, RoundedCornerShape(8.dp), clip = false)
                        .clip(RoundedCornerShape(8.dp))
                )
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
                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = item.createdAt!!.calculateTimeDifference(LocalContext.current),
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = modifier
                            .padding(end = 8.dp)
                    )
                    Text(
                        text = displayStatus,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = colorStatus,
                    )
                }
            }
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.primary,
                modifier = modifier
                    .width(24.dp)
                    .height(24.dp)
                    .clickable {
                        deleteDialog.value = true
                    }
            )
        }
        if (deleteDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    deleteDialog.value = false
                },
                title = {
                    Text(
                        text = stringResource(R.string.delete_complaint),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                text = {
                    Text(
                        text = stringResource(R.string.text_alert_delete),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            deleteClick(complaintId)
                            deleteDialog.value = false
                        }) {
                        Text(text = stringResource(R.string.delete), color = Color.White)
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            deleteDialog.value = false
                        }) {
                        Text(text = stringResource(R.string.cancel), color = Color.White)
                    }
                }
            )
        }
    }
}
