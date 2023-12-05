package com.bangkit.gocomplaint.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Add : Screen("add")
    object History : Screen("history")
    object Login : Screen("login")
    object Register : Screen("register")
}
