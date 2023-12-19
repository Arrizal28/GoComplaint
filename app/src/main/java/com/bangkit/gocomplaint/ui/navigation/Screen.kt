package com.bangkit.gocomplaint.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Add : Screen("add")
    object Login : Screen("login")
    object Register : Screen("register")
    object Profile : Screen("profile")
    object Search : Screen("search")
    object DetailComplaint : Screen("complaint/{complaintId}") {
        fun createRoute(complaintId: Int) = "complaint/$complaintId"
    }
}
