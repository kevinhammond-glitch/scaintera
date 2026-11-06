package com.example.scainterclients.screens

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.scainterclients.R
import com.example.scainterclients.models.BottomNavItem
import com.example.scainterclients.viewmodels.LoginUiState
import com.example.scainterclients.viewmodels.LoginViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun QrCode(content: String, modifier: Modifier = Modifier, tint: Color) {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    LaunchedEffect(content) {
        if (content.isNotEmpty()) {
            launch(Dispatchers.IO) {
                try {
                    val qrCodeWriter = QRCodeWriter()
                    val hints = mapOf(EncodeHintType.MARGIN to 1)
                    val bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 500, 500, hints)
                    val width = bitMatrix.width
                    val height = bitMatrix.height
                    val newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                    for (x in 0 until width) {
                        for (y in 0 until height) {
                            newBitmap.setPixel(x, y, if (bitMatrix[x, y]) tint.toArgb() else Color(0xFFE7EDEF).toArgb())
                        }
                    }
                    bitmap = newBitmap
                } catch (e: Exception) {
                    Log.e("QRCodeError", "Failed to generate QR Code", e)
                }
            }
        }
    }
    if (bitmap != null && content.isNotEmpty()) {
        Image(bitmap = bitmap!!.asImageBitmap(), contentDescription = "QR Code", modifier = modifier)
    } else {
        Spacer(modifier = modifier)
    }
}

@Composable
fun Home(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    startTab: String?
) {
    val startDestination = startTab ?: "home"
    val homeNavController = rememberNavController()

    val uriHandler = androidx.compose.ui.platform.LocalUriHandler.current

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navItems = listOf(
        BottomNavItem("Home", ImageVector.vectorResource(id = R.drawable.home), "home"),
        BottomNavItem("Consult...", ImageVector.vectorResource(id = R.drawable.stethoscope),  "consultations"),
        BottomNavItem("Examens", ImageVector.vectorResource(id = R.drawable.vitalsigns), "examinations"),
        BottomNavItem("Hospit...", ImageVector.vectorResource(id = R.drawable.hotel),  "hospitalisations")
    )

    val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val loginUiState by loginViewModel.loginState.collectAsStateWithLifecycle()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet (
                modifier = Modifier.width(350.dp),
                drawerContainerColor = Color(0xFF053f55),
                drawerShape = androidx.compose.ui.graphics.RectangleShape
                )
            {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Image(
                        painter = androidx.compose.ui.res.painterResource(id = com.example.scainterclients.R.drawable.newmobilelogoapp),
                        contentDescription = "App Logo",
                        modifier = Modifier.size(30.dp)
                    )

                    Text(
                        text = "My SCA Clients",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
                HorizontalDivider(color = Color.White.copy(alpha = 0.4f))
                val itemColors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = Color.Transparent,
                    selectedContainerColor = Color.White,
                    unselectedIconColor = Color.White,
                    unselectedTextColor = Color.White,
                    selectedIconColor = Color(0xFFE7EDEF),
                    selectedTextColor = Color(0xFFE7EDEF)
                )
                Spacer(modifier = Modifier.height(5.dp))

                NavigationDrawerItem(
                    label = { Text("À propos de nous", fontSize = 12.sp, lineHeight = 16.sp) },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            navController.navigate("contact")
                        }
                    },
                    icon = { Icon(imageVector = Icons.Default.Person, contentDescription = "Headset Icon") },
                    colors = itemColors
                )
                NavigationDrawerItem(
                    label = { Text("Mon réseau de soins", fontSize = 12.sp, lineHeight = 16.sp)},
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()

                            val state = loginUiState
                            if (state is LoginUiState.Success) {

                                val reseaux = state.reseaux

                                when (reseaux) {
                                    "VIP" -> uriHandler.openUri("https://www.scaintera-segam.com/Reseau/reseauVIP.pdf")
                                    "ECO" -> uriHandler.openUri("https://www.scaintera-segam.com/Reseau/reseauECO.pdf")
                                    "Classic" -> uriHandler.openUri("https://www.scaintera-segam.com/Reseau/reseauCLASSIC.pdf")
                                    else -> Log.e("Navigation", "Unknown reseaux type: $reseaux")
                                }
                            }
                        }
                    },
                    icon = { Icon(Icons.Default.LocalHospital, contentDescription = "Réseau de soins") },
                    colors = itemColors
                )
                NavigationDrawerItem(
                    label = { Text("Les pharmacies de garde", fontSize = 12.sp, lineHeight = 16.sp) },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            uriHandler.openUri("https://www.scaintera-segam.com/Reseau/pharmacies.pdf")
                        }
                    },
                    icon = { Icon(Icons.Default.MedicalServices, contentDescription = "pharmacies") },
                    colors = itemColors
                )
                NavigationDrawerItem(
                    label = { Text("Mes remboursements", fontSize = 12.sp, lineHeight = 16.sp) },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            navController.navigate("remboursement")
                        }
                    },
                    icon = { Icon(
                        imageVector = Icons.AutoMirrored.Filled.Assignment,
                        contentDescription = "Remboursement Icon"
                    ) },
                    colors = itemColors
                )
                NavigationDrawerItem(
                    label = { Text("Se déconnecter", fontSize = 12.sp, lineHeight = 16.sp) },
                    selected = false,
                    onClick = {
                        scope.launch {
                             drawerState.close()
                             loginViewModel.logout()
                             navController.navigate("login") {
                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    },
                    icon = { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null) },
                    colors = itemColors
                )
            }
        }
    ) {
        Scaffold(
            bottomBar = {
                NavigationBar(containerColor = Color(0xFFE7EDEF)) {
                    navItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label, fontSize = 10.sp, fontWeight = FontWeight.Normal) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                homeNavController.navigate(item.route) {
                                    popUpTo(homeNavController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF053f55),
                                unselectedIconColor = Color.Black,
                                selectedTextColor = Color(0xFF053f55),
                                unselectedTextColor = Color.Black,
                                indicatorColor = Color.White
                            )
                        )
                    }
                }
            }
    ) { innerPadding ->
         NavHost(
            navController = homeNavController,
            startDestination = startDestination,
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 8.dp)
        ) {
            composable("home") {
                 Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    when (val state = loginUiState) {
                        is LoginUiState.Success -> {
                            // Pass the scope and drawerState to the content to trigger open
                            HomeScreenContent(
                                state = state,
                                onMenuClick = { scope.launch { drawerState.open() } }
                            )
                        }
                        is LoginUiState.Loading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                        is LoginUiState.Error -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(state.message, color = Color.Red, textAlign = TextAlign.Center) }
                        is LoginUiState.Idle -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Idle State...") }
                    }
                }
            }
            composable("consultations") {
                val state = loginUiState
                if (state is LoginUiState.Success) {
                    Consultations(
                        assureID = state.assureID,
                        onConsultationClick = { consultationID ->
                            navController.navigate("consultation_details/$consultationID")
                        }
                    )
                } else {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("User data not available.")
                    }
                }
            }
            composable("examinations") {
                val state = loginUiState
                if (state is LoginUiState.Success) {
                    Examinations(
                        assureID = state.assureID,
                        onExamenClick = { examenID ->
                            navController.navigate("examen_details/$examenID")
                        }
                    )
                } else {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("User data not available.")
                    }
                }
            }
            composable("hospitalisations") {
                val state = loginUiState
                if (state is LoginUiState.Success) {
                    Hospitalisations(
                        assureID = state.assureID,
                        onHospitClick = { hospitID ->
                            navController.navigate("hospit_details/$hospitID")
                        }
                    )
                } else {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("User data not available.")
                    }
                }
            }
        }
    }
    }
}

@Composable
fun HomeScreenContent(state: LoginUiState.Success, modifier: Modifier = Modifier, onMenuClick: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 10.dp, top = 14.dp, bottom = 12.dp, end = 10.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart)
            {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color(0xFF053f55)
                    )
                }
            }
            Text(
                text = "Bienvenue,",
                fontSize = 12.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd)
            {
                IconButton(
                    onClick = { /* TODO: Navigate to notification screen */ },
                    modifier = Modifier.size(48.dp)
                ) {
                    BadgedBox(
                        badge = {
                            Badge (modifier = Modifier.offset(x = (-6).dp, y = 6.dp))
                            { Text("3") }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = Color(0xFF053f55),
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }
        }
        Text(
            text = state.fullname,
            fontSize = 16.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        QrCode(content = state.familleID.toString(), modifier = Modifier.size(160.dp), tint = Color(0xFF053f55))
        Spacer(modifier = Modifier.height(15.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(10.dp))
                .padding(start = 8.dp, top = 10.dp, bottom = 16.dp, end = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 6.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.person),
                    contentDescription = "Person Icon",
                    tint = Color(0xFF053f55),
                    modifier = Modifier.size(25.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Mon profil",
                    lineHeight = 16.sp,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Statut:", lineHeight = 16.sp, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp))
            Text(
                text = state.statut,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                modifier = Modifier.padding(start = 8.dp),
                fontStyle = FontStyle.Italic,
                color = Color.Red
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Affiliation:", lineHeight = 16.sp, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp))
            Text(
                text = state.affiliation,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = Color(0xFF053f55),
                modifier = Modifier.padding(start = 8.dp),
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Réseau de soins:", lineHeight = 16.sp, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp))
            Text(
                text = state.reseaux,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = Color(0xFF053f55),
                modifier = Modifier.padding(start = 8.dp),
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Souscripteur:", lineHeight = 16.sp, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp))
            Text(
                text = state.souscripteur,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = Color(0xFF053f55),
                modifier = Modifier.padding(start = 8.dp),
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Couverture:", lineHeight = 16.sp, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp))
            Text(
                text = "Frais externes: ${state.fraisexternes} Hospitalisation: ${state.hospitalisation}.",
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = Color(0xFF053f55),
                modifier = Modifier.padding(start = 8.dp),
                fontStyle = FontStyle.Italic
            )
        }
    }
}