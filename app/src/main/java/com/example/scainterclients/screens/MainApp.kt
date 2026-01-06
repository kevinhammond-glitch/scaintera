package com.example.scainterclients.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.scainterclients.Routes
import com.example.scainterclients.viewmodels.LoginViewModel // Import your ViewModel

@Composable
fun MainApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    // Create the ViewModel instance scoped to the NavHost
    val loginViewModel: LoginViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.Login.route,
        modifier = modifier
    ) {
        // Login
        composable(Routes.Login.route) {
            // Pass the shared ViewModel to the Login screen
            Login(navController = navController)
        }
        // Forgot
        composable(Routes.Forgot.route) {
            Forgot()
        }
        // Home
        composable(Routes.Home.route) {
            Home()
        }
    }
}
