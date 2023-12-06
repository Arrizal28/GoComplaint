package com.bangkit.gocomplaint

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bangkit.gocomplaint.ui.components.MyBottomBar
import com.bangkit.gocomplaint.ui.navigation.Screen
import com.bangkit.gocomplaint.ui.screen.home.HomeScreen
import com.bangkit.gocomplaint.ui.theme.GoComplaintTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.bangkit.gocomplaint.ui.components.MyFloatingButton
import com.bangkit.gocomplaint.ui.screen.add.AddScreen
import com.bangkit.gocomplaint.ui.screen.history.HistoryScreen
import com.bangkit.gocomplaint.ui.screen.login.LoginScreen
import com.bangkit.gocomplaint.ui.screen.register.RegisterScreen

@Composable
fun GoComplaintApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute == Screen.Home.route || currentRoute == Screen.History.route)
                MyBottomBar(navController = navController)
        },
        floatingActionButton = {
            if (currentRoute == Screen.Add.route)
                MyFloatingButton(onClick = { /*TODO*/ })
        },
        modifier = modifier,
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    navigateToRegister = {
                        navController.navigate(Screen.Login.route)
                    }
                )
            }
            composable(Screen.Add.route) {
                AddScreen()
            }
            composable(Screen.History.route) {
                HistoryScreen()
            }
            composable(Screen.Login.route) {
                LoginScreen(
                    navigateToRegister = {
                        navController.navigate(Screen.Register.route)
                    }
                )
            }
            composable(Screen.Register.route) {
                RegisterScreen()
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun GoComplaintAppPreview() {
    GoComplaintTheme {
        GoComplaintApp()
    }
}