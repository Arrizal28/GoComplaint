package com.bangkit.gocomplaint.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.PagingData
import com.bangkit.gocomplaint.R
import com.bangkit.gocomplaint.ViewModelFactory
import com.bangkit.gocomplaint.data.model.ComplaintsItem
import com.bangkit.gocomplaint.ui.components.ComplaintList
import com.bangkit.gocomplaint.ui.theme.poppinsFontFamily
import kotlinx.coroutines.flow.Flow

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    ),
    navigateToRegister: () -> Unit,
    navigateToDetail: (Int) -> Unit
) {

//    LaunchedEffect(Unit) {
//        viewModel.getSession().collect { user ->
//            if (user.token == "") {
//                navigateToRegister()
//            }
//        }
//    }

    HomeContent(
        modifier = modifier,
        listComplaints = viewModel.complaint,
        search = { },
        navigateToDetail = navigateToDetail
    )

//    val uiComplaintState by viewModel.uiComplaintState.collectAsState()

//    when (uiComplaintState) {
//        is UiState.Loading -> {
//            Loading()
//        }
//        is UiState.Success -> {
//            val complaintResponse = (uiComplaintState as UiState.Success<ComplaintResponse>).data
//        }
//        is UiState.Error -> {
//            Error()
//        }
//    }
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    listComplaints: Flow<PagingData<ComplaintsItem>>,
    search: () -> Unit,
    navigateToDetail: (Int) -> Unit
) {
    var query by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiary)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(84.dp)
                .background(
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                shape = RoundedCornerShape(size = 10.dp),
                modifier = modifier
                    .weight(1f)
                    .padding(16.dp),
                placeholder = {
                    Text(
                        text = stringResource(R.string.plchldr_search),
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                },
                trailingIcon = {
                    val image = Icons.Outlined.Search

                    val description = "Search"

                    IconButton(onClick = { search() }) {
                        Icon(imageVector = image, description)
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTrailingIconColor = Color.White,
                    unfocusedTrailingIconColor = Color.White,
                )
            )
            Image(
                painter = painterResource(R.drawable.default_profile_icon_24),
                contentDescription = "profile",
                modifier = modifier
                    .padding(top = 16.dp, end = 16.dp, bottom = 16.dp)
                    .size(56.dp)
                    .clip(CircleShape)
            )
        }
        ComplaintList(
            modifier = modifier,
            listComplaints = listComplaints,
            navigateToDetail = navigateToDetail
        )
    }
}

//@Preview
//@Composable
//fun PreviewHomeScreen() {
//    GoComplaintTheme {
//        HomeScreen(navigateToRegister = { })
//    }
//}