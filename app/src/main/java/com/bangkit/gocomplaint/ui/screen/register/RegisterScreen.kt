package com.bangkit.gocomplaint.ui.screen.register

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bangkit.gocomplaint.R
import com.bangkit.gocomplaint.ViewModelFactory
import com.bangkit.gocomplaint.data.model.RegisterRequest
import com.bangkit.gocomplaint.ui.common.UiState
import com.bangkit.gocomplaint.ui.components.BasicButton
import com.bangkit.gocomplaint.ui.screen.Error
import com.bangkit.gocomplaint.ui.screen.Loading
import com.bangkit.gocomplaint.ui.theme.poppinsFontFamily

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    ),
    navigateToLogin: () -> Unit,
    navigateToHome: () -> Unit
) {
    viewModel.getAccessToken()

    val uiState by viewModel.uiState.collectAsState()
    val uiRegisState by viewModel.uiRegisState.collectAsState()

    when (uiState) {
        is UiState.Loading -> {
            Loading()
        }

        is UiState.Success -> {
            if (uiRegisState?.token != "") {
                Toast.makeText(
                    LocalContext.current,
                    R.string.registration_successful,
                    Toast.LENGTH_SHORT
                ).show()
                navigateToHome()
            }
        }

        is UiState.Error -> {
            Error()
        }
    }

    RegisterScreenContent(
        modifier = modifier,
        navigateToLogin = navigateToLogin,
        onClick = {
            viewModel.register(
                RegisterRequest(
                    username = it.username,
                    email = it.email,
                    password = it.password,
                    confPassword = it.confPassword
                )
            )
        }
    )
}

@Composable
fun RegisterScreenContent(
    modifier: Modifier = Modifier,
    onClick: (RegisterRequest) -> Unit,
    navigateToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confPasswordVisible by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    fun validPassword(pw: String) {
        passwordError = pw.length < 8
    }

    fun validEmail(email: String) {
        isError = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    val isNotMatching = remember {
        derivedStateOf {
            password != confPassword
        }
    }

    val disableButton = remember {
        derivedStateOf {
            email.isEmpty() || username.isEmpty() || password.isEmpty() || confPassword.isEmpty() || isNotMatching.value || isError
        }
    }

    val registrationInfo = RegisterRequest(
        email = email,
        username = username,
        password = password,
        confPassword = confPassword
    )

    Box(modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.background_page),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = modifier.matchParentSize()
        )
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.heading_regis),
            color = Color.Black,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp
        )
        Text(
            text = stringResource(R.string.subhead_regis),
            color = MaterialTheme.colorScheme.onPrimary,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = modifier.padding(top = 24.dp, bottom = 48.dp)
        )
        OutlinedTextField(
            value = registrationInfo.username,
            onValueChange = { username = it },
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(size = 10.dp),
            placeholder = {
                Text(
                    text = stringResource(R.string.full_name),
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            ),
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                focusedContainerColor = MaterialTheme.colorScheme.tertiary,
                unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
                disabledContainerColor = MaterialTheme.colorScheme.tertiary,
                focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
            )
        )
        OutlinedTextField(
            value = registrationInfo.email,
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
                    color = MaterialTheme.colorScheme.onPrimary
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            ),
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                focusedContainerColor = MaterialTheme.colorScheme.tertiary,
                unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
                disabledContainerColor = MaterialTheme.colorScheme.tertiary,
                focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                errorContainerColor = MaterialTheme.colorScheme.tertiary,
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
            value = registrationInfo.password,
            onValueChange = {
                password = it
                validPassword(it)
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(size = 10.dp),
            placeholder = {
                Text(
                    text = stringResource(R.string.plchldr_pw),
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onPrimary
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
                focusedContainerColor = MaterialTheme.colorScheme.tertiary,
                unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
                disabledContainerColor = MaterialTheme.colorScheme.tertiary,
                focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                focusedTrailingIconColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.onPrimary,
                errorContainerColor = MaterialTheme.colorScheme.tertiary,
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
        OutlinedTextField(
            value = registrationInfo.confPassword,
            onValueChange = { confPassword = it },
            modifier = modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(size = 10.dp),
            placeholder = {
                Text(
                    text = stringResource(R.string.plchldr_pw),
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            },
            visualTransformation = if (confPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Go,
            ),
            trailingIcon = {
                val image = if (confPasswordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                val description = if (confPasswordVisible) "Hide password" else "Show password"

                IconButton(onClick = { confPasswordVisible = !confPasswordVisible }) {
                    Icon(imageVector = image, description)
                }
            },
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                focusedContainerColor = MaterialTheme.colorScheme.tertiary,
                unfocusedContainerColor = MaterialTheme.colorScheme.tertiary,
                disabledContainerColor = MaterialTheme.colorScheme.tertiary,
                focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                focusedTrailingIconColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.onPrimary,
                errorContainerColor = MaterialTheme.colorScheme.tertiary,
                errorIndicatorColor = MaterialTheme.colorScheme.tertiary,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
            ),
            isError = isNotMatching.value,
            supportingText = {
                if (isNotMatching.value) {
                    Text(
                        text = stringResource(R.string.password_not_match),
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = Color.Red
                    )
                }
            }
        )
        BasicButton(
            text = stringResource(R.string.register),
            onClick = { onClick(registrationInfo) },
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
                modifier = modifier.clickable { navigateToLogin() },
                text = stringResource(R.string.regis_to_login),
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.ExtraLight,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = stringResource(R.string.login),
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.ExtraLight,
                fontSize = 12.sp,
                color = Color.Blue
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun RegisterScreenPreview() {
//    GoComplaintTheme {
//        RegisterScreenContent()
//    }
//}

