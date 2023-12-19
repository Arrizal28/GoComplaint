package com.bangkit.gocomplaint.ui.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bangkit.gocomplaint.R
import com.bangkit.gocomplaint.ViewModelFactory
import com.bangkit.gocomplaint.data.model.ComplaintResponse
import com.bangkit.gocomplaint.ui.common.UiState
import com.bangkit.gocomplaint.ui.components.ProfileList
import com.bangkit.gocomplaint.ui.components.LogoutItem
import com.bangkit.gocomplaint.ui.components.ProfileItem
import com.bangkit.gocomplaint.ui.screen.Error
import com.bangkit.gocomplaint.ui.screen.Loading
import com.bangkit.gocomplaint.ui.theme.poppinsFontFamily

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    ),
    navigateToLogin: () -> Unit,
) {
    var id by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getSession().collect { user ->
            if (user.userId != 0) {
                id = user.userId.toString()
                viewModel.getProfile(id)
            }
        }
    }

    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                Loading()
            }

            is UiState.Success -> {
                ProfileContent(
                    item = uiState.data,
                    onClick = {
                        viewModel.logOut()
                        navigateToLogin()
                    }
                )
            }

            is UiState.Error -> {
                Error()
            }
        }
    }
}

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    item: ComplaintResponse
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.tertiary),
    ) {
        Box {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.menu_Profile),
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
        ProfileItem(username = item.complaints[0].username!!, modifier = modifier.padding(top = 8.dp, bottom = 8.dp))
        LogoutItem(modifier = modifier.padding(bottom = 8.dp),
            onClick = {})
        ProfileList(
            modifier = modifier,
            item = item
        )
    }
}

//@Preview
//@Composable
//fun ProfileScreenPreview() {
//    GoComplaintTheme {
//        ProfileContent()
//    }
//}