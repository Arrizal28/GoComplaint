package com.bangkit.gocomplaint.ui.screen.detail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bangkit.gocomplaint.R
import com.bangkit.gocomplaint.ViewModelFactory
import com.bangkit.gocomplaint.data.model.AddCommentRequest
import com.bangkit.gocomplaint.data.model.DetailResponse
import com.bangkit.gocomplaint.ui.common.UiState
import com.bangkit.gocomplaint.ui.components.CommentList
import com.bangkit.gocomplaint.ui.components.DetailComplaintItem
import com.bangkit.gocomplaint.ui.screen.Loading
import com.bangkit.gocomplaint.ui.theme.poppinsFontFamily

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    complaintId: Int,
    viewModel: DetailViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    ),
) {
    var query by remember { mutableStateOf("") }

    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                LaunchedEffect(Unit) {
                    viewModel.getDetailComplaint(complaintId.toString())
                }
                Loading()
            }

            is UiState.Success -> {
                DetailContent(
                    onBackClick = onBackClick,
                    item = uiState.data,
                    query = query,
                    onQueryChange = { query = it },
                    onClick = {
                        if (query != "") {
                            viewModel.addComment(
                                AddCommentRequest(
                                    id = complaintId.toString(),
                                    comment = query
                                )
                            )
                            query = ""
                        }
                    }
                )
            }

            is UiState.Error -> {
                Toast.makeText(
                    LocalContext.current,
                    stringResource(R.string.err_load), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    viewModel.uiCommentState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
            }
            is UiState.Success -> {
                LaunchedEffect(Unit) {
                    viewModel.getDetailComplaint(complaintId.toString())
                }
            }
            is UiState.Error -> {
                Toast.makeText(
                    LocalContext.current,
                    stringResource(R.string.err_action), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}


@Composable
fun DetailContent(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    item: DetailResponse,
    onClick: () -> Unit,
    query: String,
    onQueryChange: (String) -> Unit,
) {
    val displayStatus = when (item.complaint.status) {
        "O" -> "Open"
        "P" -> "Pending"
        "Y" -> "Complete"
        "N" -> "Closed"
        else -> "Unknown"
    }

    val colorStatus = when (item.complaint.status) {
        "O" -> Color(0xFF6C9BCF)
        "P" -> Color(0xFFF7C52E)
        "Y" -> Color(0xFF1B9C85)
        "N" -> Color(0xFFFF0060)
        else -> Color(0xFF000000)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.tertiary),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(R.string.back),
                tint = Color.White,
                modifier = Modifier
                    .padding(12.dp)
                    .clickable { onBackClick() }
            )
            Text(
                text = stringResource(R.string.complaint),
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color.White
            )
        }
        Column(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            DetailComplaintItem(
                username = item.complaint.username,
                date = item.complaint.createdAt,
                complaint = item.complaint.complaint,
                status = displayStatus,
                location = item.complaint.location,
                file = item.complaint.file,
                colorStatus = colorStatus,
            )
            CommentList(
                item = item
            )
        }
        CommentBox(
            query = query,
            onQueryChange = onQueryChange,
            onClick = { onClick() }
        )
    }
}

@Composable
fun CommentBox(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = modifier
                .weight(1f)
                .fillMaxHeight(),
            maxLines = 3,
            placeholder = {
                Text(
                    text = stringResource(R.string.add_a_comment),
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.tertiary
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Send,
            ),
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.tertiary,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.tertiary,
                focusedPlaceholderColor = MaterialTheme.colorScheme.tertiary,
                cursorColor = MaterialTheme.colorScheme.tertiary,
            )
        )
        Button(
            onClick = {
                onClick()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
            ),
            shape = RoundedCornerShape(size = 10.dp),
            modifier = modifier.padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.Send, contentDescription = "Send")
        }
    }
}