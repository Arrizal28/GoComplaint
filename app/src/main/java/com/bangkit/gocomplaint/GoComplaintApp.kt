package com.bangkit.gocomplaint

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bangkit.gocomplaint.ui.components.MyBottomBar
import com.bangkit.gocomplaint.ui.navigation.Screen
import com.bangkit.gocomplaint.ui.screen.add.AddScreen
import com.bangkit.gocomplaint.ui.screen.detail.DetailScreen
import com.bangkit.gocomplaint.ui.screen.home.HomeScreen
import com.bangkit.gocomplaint.ui.screen.home.HomeViewModel
import com.bangkit.gocomplaint.ui.screen.login.LoginScreen
import com.bangkit.gocomplaint.ui.screen.profile.ProfileScreen
import com.bangkit.gocomplaint.ui.screen.register.RegisterScreen
import com.bangkit.gocomplaint.ui.screen.search.SearchScreen
import com.bangkit.gocomplaint.ui.theme.GoComplaintTheme

@Composable
fun GoComplaintApp(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    ),
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val startDestination = viewModel.startDestination.collectAsState().value

    Scaffold(
        bottomBar = {
            if (currentRoute == Screen.Home.route || currentRoute == Screen.Profile.route || currentRoute == Screen.Search.route)
                MyBottomBar(navController = navController)
        },
        modifier = modifier,
    ) { innerPadding ->
        if (startDestination != null) {
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(
                        navigateToRegister = {
                            navController.navigate(Screen.Register.route)
                        },
                        navigateToDetail = { complaintId ->
                            navController.navigate(Screen.DetailComplaint.createRoute(complaintId))
                        }
                    )
                }
                composable(Screen.Search.route) {
                    SearchScreen(
                        navigateToDetail = { complaintId ->
                            navController.navigate(Screen.DetailComplaint.createRoute(complaintId))
                        }
                    )
                }
                composable(Screen.Add.route) {
                    AddScreen(
                        navigateBack = {
                            navController.navigateUp()
                        },
                        navigate = {
                            navController.navigate(Screen.Home.route)
                        }
                    )
                }
                composable(Screen.Profile.route) {
                    ProfileScreen(
                        navigateToLogin = {
                            navController.navigate(Screen.Login.route)
                        },
                        navigateToDetail = { complaintId ->
                            navController.navigate(Screen.DetailComplaint.createRoute(complaintId))
                        }
                    )
                }
                composable(Screen.Login.route) {
                    LoginScreen(
                        navigateToHome = {
                            navController.navigate(Screen.Home.route)
                        },
                        navigateToRegister = {
                            navController.navigate(Screen.Register.route)
                        }
                    )
                }
                composable(Screen.Register.route) {
                    RegisterScreen(
                        navigateToHome = {
                            navController.navigate(Screen.Home.route)
                        },
                        navigateToLogin = {
                            navController.navigate(Screen.Login.route)
                        }
                    )
                }
                composable(
                    route = Screen.DetailComplaint.route,
                    arguments = listOf(navArgument("complaintId") { type = NavType.IntType }),
                ) {
                    val id = it.arguments?.getInt("complaintId") ?: -1
                    DetailScreen(
                        complaintId = id,
                        onBackClick = {
                            navController.navigateUp()
                        },
                    )
                }
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