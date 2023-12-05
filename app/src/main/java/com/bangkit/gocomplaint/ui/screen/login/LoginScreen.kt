package com.bangkit.gocomplaint.ui.screen.login

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bangkit.gocomplaint.R
import com.bangkit.gocomplaint.ui.components.BasicButton
import com.bangkit.gocomplaint.ui.navigation.Screen
import com.bangkit.gocomplaint.ui.theme.GoComplaintTheme
import com.bangkit.gocomplaint.ui.theme.poppinsFontFamily

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navigateToRegister: () -> Unit
) {
    LoginScreenContent(
        onClick = {
            navigateToRegister()
        }
    )
}

@Composable
fun LoginScreenContent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

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
            value = email,
            onValueChange = { email = it },
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
            )
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
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
            )
        )
        BasicButton(
            text = stringResource(R.string.login),
            onClick = {},
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
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
                modifier = modifier.clickable{ onClick() },
                text = stringResource(R.string.register),
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.ExtraLight,
                fontSize = 12.sp,
                color = Color.Blue
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview(
    navController: NavHostController = rememberNavController(),
) {
    GoComplaintTheme {
        LoginScreen(
            navigateToRegister = { navController.navigate(Screen.Register.route) }
        )
    }
}