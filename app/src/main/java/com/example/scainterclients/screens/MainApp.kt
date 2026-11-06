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
import com.example.scainterclients.viewmodels.LoginViewModel

@Composable
fun MainApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.Login.route,
        modifier = modifier
    ) {
        // Login
        composable(Routes.Login.route) {
            Login(navController = navController,
                loginViewModel = loginViewModel)
        }
        // Forgot
        composable(Routes.Forgot.route) {
            Forgot(navController = navController)
        }
        // Contact
        composable(Routes.Contact.route) {
            Contact(navController = navController)
        }
        // Remboursement
        composable(Routes.Remboursement.route) {
            Remboursement(navController = navController)
        }
        // Remboursementdemande
        composable(Routes.Remboursementdemande.route) {
            Remboursementdemande(navController = navController)
        }
        // Home
        composable(
            route = "home?start_tab={start_tab}",
            arguments = listOf(navArgument("start_tab") {
                type = NavType.StringType
                nullable = true
            })
        ) { backStackEntry ->
            // Extract the argument
            val startTab = backStackEntry.arguments?.getString("start_tab")
            Home(
                navController = navController,
                loginViewModel = loginViewModel,
                startTab = startTab // Pass it to the Home screen
            )
        }
        composable(
            route = "consultation_details/{consultationID}",
            arguments = listOf(navArgument("consultationID") { type = NavType.IntType })
        ) { backStackEntry ->
            // Retrieve the ID from the navigation arguments. [2]
            val consultationID = backStackEntry.arguments?.getInt("consultationID")
            requireNotNull(consultationID) { "consultationID parameter was not provided." }

            ConsultationDetailScreen(
                navController = navController,
                consultationID = consultationID,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(
            route = "examen_details/{examenID}",
            arguments = listOf(navArgument("examenID") { type = NavType.IntType })
        ) { backStackEntry ->
            // Retrieve the ID from the navigation arguments. [2]
            val examenID = backStackEntry.arguments?.getInt("examenID")
            requireNotNull(examenID) { "examenID parameter was not provided." }

            ExaminationDetailScreen(
                navController = navController,
                examenID = examenID,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(
            route = "hospit_details/{hospitID}",
            arguments = listOf(navArgument("hospitID") { type = NavType.IntType })
        ) { backStackEntry ->
            // Retrieve the ID from the navigation arguments. [2]
            val hospitID = backStackEntry.arguments?.getInt("hospitID")
            requireNotNull(hospitID) { "hospitID parameter was not provided." }

            HospitalisationDetailScreen(
                navController = navController,
                hospitID = hospitID,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
