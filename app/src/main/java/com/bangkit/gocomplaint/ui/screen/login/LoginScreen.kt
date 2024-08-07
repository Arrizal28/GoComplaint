package com.bangkit.gocomplaint.ui.screen.login

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bangkit.gocomplaint.R
import com.bangkit.gocomplaint.ViewModelFactory
import com.bangkit.gocomplaint.data.model.LoginRequest
import com.bangkit.gocomplaint.ui.common.UiState
import com.bangkit.gocomplaint.ui.components.BasicButton
import com.bangkit.gocomplaint.ui.screen.Loading
import com.bangkit.gocomplaint.ui.theme.poppinsFontFamily
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    ),
    navigateToRegister: () -> Unit,
    navigateToHome: () -> Unit
) {
    val uiLoginState by viewModel.uiLoginState.collectAsState()

    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var snackbarSuccess by remember { mutableStateOf(false) }

    fun showSnackbar(message: String) {
        scope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getAccessToken()
    }

    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                Log.d("LoginScreen", "Loading")
                Loading()
            }

            is UiState.Success -> {
                if (uiLoginState?.token != "") {
                    snackbarSuccess = true
                    showSnackbar(context.getString(R.string.login_successfully))
                    navigateToHome()
                }
            }

            is UiState.Error -> {
                Log.d("LoginScreen", "Error: ${uiState.errorMessage}")
                showSnackbar((uiState).errorMessage)
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(
                    snackbarData = it,
                    containerColor = if (snackbarSuccess) Color.Green else Color.Red
                )
            }
        },
    ) {
        LoginScreenContent(
            navigateToRegister = { navigateToRegister() },
            onClick = {
                viewModel.login(
                    LoginRequest(
                        email = it.email,
                        password = it.password,
                    )
                )
            }
        )
    }
}

@Composable
fun LoginScreenContent(
    modifier: Modifier = Modifier,
    onClick: (LoginRequest) -> Unit,
    navigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var isError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    fun validPassword(pw: String) {
        passwordError = pw.length < 8
    }

    fun validEmail(email: String) {
        isError = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    val disableButton = remember {
        derivedStateOf {
            email.isEmpty() || password.isEmpty() || isError || passwordError
        }
    }

    val loginInfo = LoginRequest(
        email = email,
        password = password,
    )

    val scrollState = rememberScrollState()

    Box(modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.background_page),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = modifier.matchParentSize()
        )

        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.heading_login),
                color = Color.Black,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )
            Text(
                text = stringResource(R.string.subhead_login),
                color = MaterialTheme.colorScheme.onPrimary,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = modifier.padding(top = 24.dp, bottom = 48.dp)
            )
            OutlinedTextField(
                value = loginInfo.email,
                onValueChange = {
                    email = it
                    validEmail(it)
                },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(size = 10.dp),
                placeholder = {
                    Text(
                        text = stringResource(R.string.plchldr_email),
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        color = Color(0xff979797)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    focusedContainerColor = Color(0xffE3E3E3),
                    unfocusedContainerColor = Color(0xffE3E3E3),
                    disabledContainerColor = MaterialTheme.colorScheme.tertiary,
                    focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                    errorContainerColor = Color(0xffE3E3E3),
                    errorIndicatorColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                ),
                supportingText = {
                    if (isError) {
                        Text(
                            text = stringResource(R.string.invalid_email_format),
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            color = Color.Red
                        )
                    }
                },
                isError = isError,
            )
            OutlinedTextField(
                value = loginInfo.password,
                onValueChange = {
                    password = it
                    validPassword(it)
                },
                modifier = modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(size = 10.dp),
                placeholder = {
                    Text(
                        text = stringResource(R.string.plchldr_pw),
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        color = Color(0xff979797)
                    )
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Go,
                ),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, description)
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    focusedContainerColor = Color(0xffE3E3E3),
                    unfocusedContainerColor = Color(0xffE3E3E3),
                    disabledContainerColor = MaterialTheme.colorScheme.tertiary,
                    focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                    focusedTrailingIconColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedTrailingIconColor = Color(0xff979797),
                    errorContainerColor = Color(0xffE3E3E3),
                    errorIndicatorColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                ),
                isError = passwordError,
                supportingText = {
                    if (passwordError) {
                        Text(
                            text = stringResource(R.string.password_error),
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            color = Color.Red
                        )
                    }
                }
            )
            BasicButton(
                text = stringResource(R.string.login),
                onClick = { onClick(loginInfo) },
                containerColor = MaterialTheme.colorScheme.primary,
                color = Color.White,
                fontSize = 16.sp,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                enabled = !disableButton.value
            )
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(R.string.regis_desc),
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.ExtraLight,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    modifier = modifier.clickable { navigateToRegister() },
                    text = stringResource(R.string.register),
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.ExtraLight,
                    fontSize = 12.sp,
                    color = Color.Blue
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewLoginScreen() {
    LoginScreenContent(
        onClick = {},
        navigateToRegister = {}
    )
}