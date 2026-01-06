package com.example.scainterclients

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

// Define a sealed class for your routes to hold more data
sealed class Routes(val route: String, val title: String, val icon: ImageVector) {
    object Login : Routes("login", "Login", Icons.Default.Home)

    object Home : Routes("home", "Home", Icons.Default.Home)

    object Search : Routes("search", "Search", Icons.Default.Search)

    object Profile : Routes("profile", "Profile", Icons.Default.Person)
    object Forgot : Routes("forgot", "Forgot", Icons.Default.Home) // Dummy icon
}
