package com.bangkit.gocomplaint.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.bangkit.gocomplaint.R

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToRegister: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(modifier = modifier.clickable{ navigateToRegister() },text = stringResource(R.string.menu_home))
    }
}