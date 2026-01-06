package com.example.scainterclients.screens

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.scainterclients.viewmodels.LoginUiState
import com.example.scainterclients.viewmodels.LoginViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.graphics.Color as AndroidColor


data class BottomNavItem(val label: String, val icon: ImageVector, val route: String)

@Composable
fun QrCode(content: String, modifier: Modifier = Modifier) {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(content) {
        launch(Dispatchers.IO) {
            try {
                val qrCodeWriter = QRCodeWriter()
                val bitMatrix = qrCodeWriter.encode(
                    content,
                    BarcodeFormat.QR_CODE,
                    250, // width
                    250  // height
                )
                val width = bitMatrix.width
                val height = bitMatrix.height
                val newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                for (x in 0 until width) {
                    for (y in 0 until height) {
                        newBitmap.setPixel(x, y, if (bitMatrix[x, y]) AndroidColor.BLACK else Color(0xFFE7EDEF).toArgb())
                    }
                }
                bitmap = newBitmap // Assign the generated bitmap
            } catch (e: Exception) {
                Log.e("QRCodeError", "Failed to generate QR Code", e)
            }
        }
    }

    bitmap?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = "QR Code",
            modifier = modifier
        )
    }
}

@Composable
fun Home(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    loginViewModel: LoginViewModel = viewModel()
)
{// Add a unique "route" for each navigation item
    val navItems = listOf(
        BottomNavItem("Home", Icons.Default.Home, "home"),
        BottomNavItem("Consulta...", Icons.AutoMirrored.Filled.List, "consultations"),
        BottomNavItem("Examens", Icons.Default.MonitorHeart, "examinations"),
        BottomNavItem("Hospita...", Icons.Default.Hotel, "hospitalisations")
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFE7EDEF)
            ) {
                navItems.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = {
                            Text(
                                text = item.label,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                            )
                        },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF053f55), // Teal for selected icon
                            unselectedIconColor = Color.Black,
                            selectedTextColor = Color(0xFF053f55), // Teal for selected text
                            unselectedTextColor = Color.Black,
                            indicatorColor = Color.White // Light teal for the indicator
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        val welcome = "Bienvenue,"
        val quelstatut = "Statut:"
        val quelreseaux = "Réseaux de soins:"
        val quelsouscripteur = "Souscripteur:"
        val quelcouverture = "Couverture:"
                // NavHost will display the correct screen based on the route
        NavHost(navController = navController, startDestination = "home", modifier = modifier.padding(innerPadding)) {
            composable("home") {
                // Your UI for the Home screen content
                Column(
                    modifier = Modifier
                        .fillMaxSize() // Added fillMaxSize to better center content
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp), // Adds space between items
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = welcome,
                        fontSize = 26.sp,
                        lineHeight = 36.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Famille",
                        fontSize = 26.sp,
                        lineHeight = 40.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    QrCode(content = "hello", modifier = Modifier.size(300.dp))
                    Spacer(modifier = Modifier.height(22.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth() // Make the Row take the full width
                            .background(Color.White, shape = RoundedCornerShape(4.dp))
                            .padding(4.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = quelstatut,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp), // 16dp on all four sides
                        )
                        Text(
                            text = "statut",
                            modifier = Modifier.padding(start = 8.dp), // 16dp on all four sides
                            fontStyle = FontStyle.Italic,
                            color = Color.Red

                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = quelreseaux,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp), // 16dp on all four sides
                        )
                        Text(
                            text = "reseaux",
                            modifier = Modifier.padding(start = 8.dp), // 16dp on all four sides
                            fontStyle = FontStyle.Italic

                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = quelsouscripteur,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp), // 16dp on all four sides
                        )
                        Text(
                            text = "souscripteur",
                            modifier = Modifier.padding(start = 8.dp), // 16dp on all four sides
                            fontStyle = FontStyle.Italic,

                            )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = quelcouverture,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp), // 16dp on all four sides
                        )
                        Text(
                            text = "Frais externes",
                            modifier = Modifier.padding(start = 8.dp), // 16dp on all four sides
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
            }
            composable("consultations") {
                //Consultations()
            }
            composable("examinations")  {
                 //Examinations()
            }
            composable("hospitalisations") {
                 //Hospitalisations()
            }
        }
    }
}
