package com.bangkit.gocomplaint.ui.screen.detail

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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bangkit.gocomplaint.R
import com.bangkit.gocomplaint.ViewModelFactory
import com.bangkit.gocomplaint.data.model.DetailResponse
import com.bangkit.gocomplaint.ui.common.UiState
import com.bangkit.gocomplaint.ui.components.CommentList
import com.bangkit.gocomplaint.ui.components.DetailComplaintItem
import com.bangkit.gocomplaint.ui.screen.Error
import com.bangkit.gocomplaint.ui.screen.Loading
import com.bangkit.gocomplaint.ui.theme.GoComplaintTheme
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
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                Loading()
                viewModel.getDetailComplaint(complaintId.toString())
            }

            is UiState.Success -> {
                DetailContent(
                    onBackClick = onBackClick,
                    item = uiState.data,
                )
            }

            is UiState.Error -> {
                Error()
            }
        }
    }
}


@Composable
fun DetailContent(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    item: DetailResponse
) {
    val displayStatus = when (item.complaint.status) {
        "N" -> "Open"
        "P" -> "Pending"
        "Y" -> "Complete"
        else -> "Unknown" // Menambahkan ini jika nilai status tidak sesuai dengan yang diharapkan
    }

    var query by remember { mutableStateOf("") }

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
                    color = MaterialTheme.colorScheme.secondary,
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
                status = displayStatus
            )
            CommentList(
                item = item
            )
        }
        CommentBox(
            query = query,
            onQueryChange = {query = it}
        )
    }
}

@Composable
fun CommentBox(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
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
                    color = MaterialTheme.colorScheme.onPrimary
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Send,
            ),
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
                focusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
                cursorColor = MaterialTheme.colorScheme.onPrimary,
            )
        )
        Button(
            onClick = { },
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

@Preview(showBackground = true)
@Composable
fun CommentBoxPreview() {
    GoComplaintTheme {
        CommentBox(query = "", onQueryChange = {})
    }
}