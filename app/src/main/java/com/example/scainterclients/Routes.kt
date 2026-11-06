package com.example.scainterclients

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

// Define a sealed class for your routes to hold more data
sealed class Routes(val route: String, val title: String, val icon: ImageVector) {
    object Login : Routes("login", "Login", Icons.Default.Home)
    object Contact : Routes("contact", "Contact", Icons.Default.Home)
    object Remboursement : Routes("remboursement", "Remboursement", Icons.Default.Home)
    object Remboursementdemande : Routes("remboursementdemande", "Remboursementdemande", Icons.Default.Home)
    object Home : Routes("home", "Home", Icons.Default.Home)
    object Forgot : Routes("forgot", "Forgot", Icons.Default.Home)
}
