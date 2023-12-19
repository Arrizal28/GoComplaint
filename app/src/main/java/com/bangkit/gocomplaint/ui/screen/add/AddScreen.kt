package com.bangkit.gocomplaint.ui.screen.add

import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.bangkit.gocomplaint.R
import com.bangkit.gocomplaint.ViewModelFactory
import com.bangkit.gocomplaint.data.model.AddComplaintRequest
import com.bangkit.gocomplaint.ui.common.UiState
import com.bangkit.gocomplaint.ui.components.BasicButton
import com.bangkit.gocomplaint.ui.screen.Error
import com.bangkit.gocomplaint.ui.screen.Loading
import com.bangkit.gocomplaint.ui.theme.poppinsFontFamily
import com.bangkit.gocomplaint.util.reduceFileImage
import com.bangkit.gocomplaint.util.uriToFile

//@Composable
//fun AddScreen(
//    modifier: Modifier = Modifier,
//) {
//    Box(
//        modifier = modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center,
//    ) {
//        Text(stringResource(R.string.search_not_found), color = MaterialTheme.colorScheme.onPrimary)
//    }
//}

@Composable
fun AddScreen(
    modifier: Modifier = Modifier,
    viewModel: AddViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    ),
    navigateToProfile: () -> Unit,
    navigateBack: () -> Unit,
) {
    viewModel.getAccessToken()

    val uiState by viewModel.uiState.collectAsState()
    val uiUserState by viewModel.uiUserState.collectAsState()

    when (uiState) {
        is UiState.Loading -> {
            Loading()
        }

        is UiState.Success -> {
            if (uiUserState?.token != "") {
                Toast.makeText(
                    LocalContext.current,
                    stringResource(R.string.success_add_complaint), Toast.LENGTH_SHORT
                ).show()
                navigateToProfile()
            }
        }

        is UiState.Error -> {
            Error()
        }
    }

    AddContent(onBackClick = navigateBack)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContent(
    modifier: Modifier = Modifier,
    viewModel: AddViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    ),
    onBackClick: () -> Unit
) {
    val categories = listOf(
        Category(R.string.infrastructure, "Infrastruktur"),
        Category(R.string.environment, "Lingkungan"),
        Category(R.string.transportation, "Transportasi"),
        Category(R.string.public_service, "Pelayanan Publik"),
        Category(R.string.security_and_order, "Keamanan dan Ketertiban"),
        Category(R.string.health, "Kesehatan"),
        Category(R.string.education, "Pendidikan"),
        Category(R.string.employment_and_labor, "Pekerjaan dan Ketenagakerjaan"),
        Category(R.string.settlement_and_housing, "Pemukiman dan Perumahan"),
        Category(R.string.social_and_welfare, "Sosial dan Kesejahteraan"),
    )
    var text by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(categories[0].apiValue) }
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val context = LocalContext.current


    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            selectedImageUri = it
        }
    )

//    val addComplaintInfo = AddComplaintRequest(
//        complaint = text,
//        category = selectedCategory,
//        location = address
//    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.tertiary)
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
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Outlined.Cancel,
                contentDescription = stringResource(R.string.publish),
                modifier = modifier
                    .padding(start = 16.dp)
                    .size(24.dp)
                    .clickable { onBackClick() },
                tint = Color.White
            )
            BasicButton(
                text = stringResource(R.string.publish),
                onClick = {
                    selectedImageUri?.let { uri ->
                        val imageFile =
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                uriToFile(uri, context).reduceFileImage()
                            } else {
                                uriToFile(uri, context)
                            }
                        viewModel.addComplaint(
                            complaint = text,
                            category = selectedCategory,
                            location = address,
                            file = imageFile
                        )
                    }
                },
                fontSize = 8.sp,
                color = MaterialTheme.colorScheme.primary,
                containerColor = Color.White,
                modifier = modifier
                    .padding(16.dp)
                    .height(28.dp)
                    .width(80.dp),
                enabled = true
            )
        }
        Row(
            modifier = modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.tertiary,
                )
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(R.drawable.default_profile_icon_24),
                contentDescription = "profile",
                modifier = modifier
                    .padding(start = 16.dp, top = 16.dp)
                    .size(48.dp)
                    .clip(CircleShape)
            )
            Column {
                TextField(
                    value = text,
                    onValueChange = { newText ->
                        if (newText.length <= 255) {
                            text = newText
                        }
                    },
                    modifier = modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    placeholder = {
                        Text(
                            text = stringResource(
                                R.string.plchldr_add
                            ),
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                        focusedContainerColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
                        focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                    )
                )
                Divider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                TextField(
                    value = address,
                    onValueChange = { newText ->
                        if (newText.length <= 50) {
                            address = newText
                        }
                    },
                    modifier = modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    placeholder = {
                        Text(
                            text = stringResource(R.string.address),
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                        focusedContainerColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
                        focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                    )
                )
                Divider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {
                    TextField(
                        modifier = Modifier
                            .menuAnchor(),
                        readOnly = true,
                        value = categories.find { it.apiValue == selectedCategory }?.displayText?.let {
                            stringResource(it)
                        } ?: "",
                        onValueChange = { },
                        label = { Text(stringResource(R.string.category)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(
                            focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                            unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                            focusedContainerColor = MaterialTheme.colorScheme.tertiary,
                            unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
                            focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            unfocusedTrailingIconColor = MaterialTheme.colorScheme.onPrimary,
                            focusedTrailingIconColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        },
                    ) {
                        categories.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(stringResource(selectionOption.displayText)) },
                                onClick = {
                                    selectedCategory = selectionOption.apiValue
                                    expanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
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
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    model = selectedImageUri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
        }
        Button(modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            onClick = {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }) {
            Text(
                text = "Pick a Photo",
                style = TextStyle(
                    fontSize = 18.sp
                )
            )
        }
    }
}

data class Category(val displayText: Int, val apiValue: String)

//@Preview(showBackground = true)
//@Composable
//fun AddScreenPreview() {
//    GoComplaintTheme {
//        AddContent()
//    }
//}