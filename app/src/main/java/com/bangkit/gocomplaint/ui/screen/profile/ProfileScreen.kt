package com.bangkit.gocomplaint.ui.screen.profile

import android.widget.Toast
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.bangkit.gocomplaint.ui.components.LogoutItem
import com.bangkit.gocomplaint.ui.components.ProfileItem
import com.bangkit.gocomplaint.ui.components.ProfileList
import com.bangkit.gocomplaint.ui.screen.ErrorScreen
import com.bangkit.gocomplaint.ui.screen.Loading
import com.bangkit.gocomplaint.ui.theme.poppinsFontFamily

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    ),
    navigateToLogin: () -> Unit,
    navigateToDetail: (Int) -> Unit,
) {
    val id by viewModel.stateId.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getProfile(id)
    }

    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                Loading()
            }

            is UiState.Success -> {
                ProfileContent(
                    emptyList = uiState.data.complaints.isEmpty(),
                    item = uiState.data,
                    onClick = {
                        viewModel.logOut()
                        navigateToLogin()
                    },
                    navigateToDetail = navigateToDetail,
                    deleteClick = {
                        viewModel.deleteComplaint(it)
                    },
                )
            }

            is UiState.Error -> {
                ErrorScreen(retryAction = { { /*TODO*/ } }, modifier = modifier.fillMaxSize())
            }
        }
    }

    viewModel.uiDeleteState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
            }

            is UiState.Success -> {
                viewModel.getProfile(id)
            }

            is UiState.Error -> {
                Toast.makeText(
                    LocalContext.current,
                    stringResource(R.string.err_delete), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    item: ComplaintResponse,
    navigateToDetail: (Int) -> Unit,
    emptyList: Boolean = false,
    deleteClick: (complaintId: String) -> Unit
) {

    val openDialog = remember { mutableStateOf(false) }

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
                        color = MaterialTheme.colorScheme.primary,
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
        if (item.complaints.isNotEmpty()) {
            ProfileItem(
                username = item.complaints[0].username!!,
                modifier = modifier.padding(top = 8.dp, bottom = 8.dp)
            )
        }
        LogoutItem(modifier = modifier.padding(bottom = 8.dp),
            onClick = {
                openDialog.value = true
            })

        if(!emptyList) {
            ProfileList(
                modifier = modifier,
                item = item,
                navigateToDetail = navigateToDetail,
                deleteClick = {
                    deleteClick(it)
                }
            )
        }


        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    openDialog.value = false
                },
                title = {
                    Text(
                        text = stringResource(R.string.title_alert_logout),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                text = {
                    Text(
                        text = stringResource(R.string.text_alert_logout),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onClick()
                            openDialog.value = false
                        }) {
                        Text(text = stringResource(R.string.logout), color = Color.White)
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            openDialog.value = false
                        }) {
                        Text(text = stringResource(R.string.cancel), color = Color.White)
                    }
                },
                containerColor = MaterialTheme.colorScheme.tertiary,
            )
        }
    }
}