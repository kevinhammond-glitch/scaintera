package com.example.scainterclients.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.scainterclients.R
import com.example.scainterclients.services.HospitDetails
import com.example.scainterclients.viewmodels.HospitDetailsUiState
import com.example.scainterclients.viewmodels.HospitDetailsViewModel
import com.example.scainterclients.viewmodels.HospitDetailsViewModelFactory
import com.example.scainterclients.viewmodels.RatingHospitViewModel
import com.example.scainterclients.viewmodels.RatingHospitViewModelFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Composable
fun HospitalisationDetailScreen(
    hospitID: Int, 
    onBackClick: () -> Unit,
    navController: NavHostController, 
    modifier: Modifier = Modifier
) {
    val viewModel: HospitDetailsViewModel = viewModel(
        factory = HospitDetailsViewModelFactory(hospitID)
    )
    val uiState by viewModel.detailsState.collectAsState()

    val ratingViewModel: RatingHospitViewModel = viewModel(
        factory = RatingHospitViewModelFactory(hospitID)
    )

    val submissionStatus by ratingViewModel.submissionStatus.collectAsState()
    val context = LocalContext.current

    // UI Logic to handle submission feedback
    LaunchedEffect(submissionStatus) {
        if (submissionStatus == "Success") {
            Toast.makeText(context, "Avis envoyé!", Toast.LENGTH_SHORT).show()
            ratingViewModel.resetStatus()
        } else if (submissionStatus?.startsWith("Error") == true) {
            Toast.makeText(context, submissionStatus, Toast.LENGTH_LONG).show()
        }
    }
    
    Scaffold(
        bottomBar = { AppBottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(start = 10.dp, end = 10.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Détails",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp, top = 10.dp)
            )
            when (val state = uiState) {
                is HospitDetailsUiState.Loading -> {
                    Box(modifier = Modifier
                        .fillMaxWidth() // Fill width to center the indicator
                        .padding(vertical = 50.dp), // Add some padding
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is HospitDetailsUiState.Success -> {
                    HospitDetailsContent(
                        details = state.details,
                        ratingViewModel = ratingViewModel
                    )
                }
                is HospitDetailsUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AppBottomNavigationBar(navController: NavHostController) {
    NavigationBar(
        containerColor = Color(0xFFE7EDEF),
    ) {
        // Note: The onClick for each item should handle navigation.
        // This is a simplified example.
        NavigationBarItem(
            icon = { Icon(ImageVector.vectorResource(id = R.drawable.home), contentDescription = "Home") },
            label = { Text("Home", fontSize = 10.sp, fontWeight = FontWeight.Normal) },
            selected = false,
            onClick = { navController.navigate("home") }
        )
        NavigationBarItem(
            icon = { Icon(ImageVector.vectorResource(id = R.drawable.stethoscope), contentDescription = "Hospitations") },
            label = { Text("Hospit...", fontSize = 10.sp, fontWeight = FontWeight.Normal )},
            selected = false, // Example: Mark this screen as selected
            onClick = {
                navController.navigate("home?start_tab=Hospitations") {
                    // Pop up to the home screen to clear the detail screen from the back stack
                    popUpTo("home") {
                        inclusive = true
                    }
                    // Ensure you don't create a new Home screen if one is already there
                    launchSingleTop = true
                }
            },
        )
        NavigationBarItem(
            icon = { Icon(ImageVector.vectorResource(id = R.drawable.vitalsigns), contentDescription = "Examinations") },
            label = { Text("Examens", fontSize = 10.sp, fontWeight = FontWeight.Normal) },
            selected = false,
            onClick = {
                navController.navigate("home?start_tab=examinations") {
                    // Pop up to the home screen to clear the detail screen from the back stack
                    popUpTo("home") {
                        inclusive = true
                    }
                    // Ensure you don't create a new Home screen if one is already there
                    launchSingleTop = true
                }
            },
        )
        NavigationBarItem(
            icon = { Icon(ImageVector.vectorResource(id = R.drawable.hotel), contentDescription = "Hospitalisations") },
            label = { Text("Hospit...", fontSize = 10.sp, fontWeight = FontWeight.Normal) },
            selected = true,
            onClick = {
                navController.navigate("home?start_tab=hospitalisations") {
                    // Pop up to the home screen to clear the detail screen from the back stack
                    popUpTo("home") {
                        inclusive = true
                    }
                    // Ensure you don't create a new Home screen if one is already there
                    launchSingleTop = true
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

private fun formatDisplayDate(dateString: String): String {
    // Formatter for "yyyy-MM-dd HH:mm:ss" - common in SQL databases
    val localDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    // Target format
    val outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    return try {
        // First, try parsing with the standard ISO_OFFSET_DATE_TIME
        val parsedDate = LocalDateTime.parse(dateString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        parsedDate.format(outputFormatter)
    } catch (e: DateTimeParseException) {
        try {
            // If that fails, try parsing with "yyyy-MM-dd HH:mm:ss" format
            val parsedDate = LocalDateTime.parse(dateString, localDateTimeFormatter)
            parsedDate.format(outputFormatter)
        } catch (e: Exception) {
            // If all parsing fails, return just the date part of the original string as a fallback
            dateString.substringBefore(" ")
        }
    }
}

@Composable
private fun HospitDetailsContent(
    details: HospitDetails,
    modifier: Modifier = Modifier,
    ratingViewModel: RatingHospitViewModel
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(4.dp))
        AsyncImage(
            model = "https://www.scaintera-segam.com/Photos_SCA/${details.interneID ?: ""}.jpg",
            contentDescription = "Assuré(e) profile picture",
            modifier = Modifier
                .size(140.dp)
                .shadow(elevation = 8.dp, shape = CircleShape)
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = details.fullname ?: "",
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = details.interneID ?: "",
            fontSize = 12.sp,
            lineHeight = 16.sp,
            textAlign = TextAlign.Center,
            color = Color.Red
        )
        Text(
            text = "Couverture: ${details.fraishospit ?: ""}",
            fontSize = 12.sp,
            lineHeight = 16.sp,
            textAlign = TextAlign.Center,
        )
        Text(
            text = details.sousname ?: "",
            fontSize = 12.sp,
            lineHeight = 16.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF053f55)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(10.dp))
                .padding(start = 10.dp, end = 10.dp, top = 12.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 6.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.hotel),
                    contentDescription = "hospitalisation Icon",
                    tint = Color(0xFF053f55),
                    modifier = Modifier.size(25.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Hospitalisation",
                    lineHeight = 16.sp,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Statut:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 6.dp), fontSize = 12.sp, lineHeight = 16.sp)
            Text(
                text = details.etat ?: "",
                color = Color.Red,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                modifier = Modifier.padding(start = 6.dp),
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Motif:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 6.dp), fontSize = 12.sp, lineHeight = 16.sp)
            Text(
                text = details.motif ?: "",
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = Color(0xFF053f55),
                modifier = Modifier.padding(start = 6.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Type:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 6.dp), fontSize = 12.sp, lineHeight = 16.sp)
            Text(
                text = "Hospitalisation ${details.type ?: ""}".trim(),
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = Color(0xFF053f55),
                modifier = Modifier.padding(start = 6.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Durée:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 6.dp), fontSize = 12.sp, lineHeight = 16.sp)
            Text(
                text = "${details.duree ?: ""} jours",
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = Color(0xFF053f55),
                modifier = Modifier.padding(start = 6.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Prestataire:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 6.dp), fontSize = 12.sp, lineHeight = 16.sp)
            Text(
                text = details.prestaname ?: "",
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = Color(0xFF053f55),
                modifier = Modifier.padding(start = 6.dp),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Date:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 6.dp), fontSize = 12.sp, lineHeight = 16.sp)
            Text(
                text = formatDisplayDate(details.dateCreated ?: ""),
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = Color(0xFF053f55),
                modifier = Modifier.padding(start = 6.dp),
            )

            val submissionStatus by ratingViewModel.submissionStatus.collectAsState()
            val ratingViewModel: RatingHospitViewModel = viewModel()

            var showRatingDialog by remember { mutableStateOf(false) }
            var rating by remember { mutableIntStateOf(0) }
            var comment by remember { mutableStateOf("") }

            val isAlreadyRated = (details.userRating?.toIntOrNull() ?:0) in 1..5 || submissionStatus == "Success"

            if (!isAlreadyRated) {
                // Only show the button if not rated yet
                Button(
                    onClick = { showRatingDialog = true },
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF053f55)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(text = "Notez cette prestation", color = Color.White, fontSize = 12.sp)
                }
            } else {
                // Display the rating and comment instead of the button
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .background(Color(0xFFF0F4F5), shape = RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Display the rating and comment instead of the button
                    Text(text = "Votre avis:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 6.dp), fontSize = 12.sp, lineHeight = 16.sp)
                    Row(modifier = Modifier.padding(vertical = 6.dp))
                    {
                        val finalRating = if (submissionStatus == "Success") rating else details.userRating?.toIntOrNull() ?: 0
                        repeat(5) { index ->
                            Icon(
                                imageVector = if (index < finalRating) Icons.Filled.Star else Icons.Filled.StarOutline,
                                contentDescription = null,
                                tint = Color(0xFFFFB400),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    val finalComment = (if (submissionStatus == "Success") comment else details.userComment) ?: ""
                    if (finalComment.isNotBlank()) {
                        Text(text = finalComment, color = Color(0xFF053f55), modifier = Modifier.padding(start = 6.dp), fontSize = 12.sp, lineHeight = 16.sp, textAlign = TextAlign.Justify)
                    }
                }
            }

            if (showRatingDialog) {
                AlertDialog(
                    onDismissRequest = { showRatingDialog = false },
                    containerColor = Color.White,
                    title = {
                        Text(
                            text = "Votre avis nous intéresse!",
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    text = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text("Votre avis:", fontSize = 14.sp)
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                (1..5).forEach { index ->
                                    IconButton(onClick = { rating = index }) {
                                        Icon(
                                            imageVector = if (index <= rating) Icons.Filled.Star else Icons.Filled.StarOutline,
                                            contentDescription = null,
                                            tint = if (index <= rating) Color(0xFFFFB400) else Color.Gray
                                        )
                                    }
                                }
                            }

                            OutlinedTextField(
                                value = comment,
                                onValueChange = { comment = it },
                                label = { Text("Votre commentaire (optionnel)", fontSize = 12.sp, color = Color(0xFF053f55)) },
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = TextStyle(fontSize = 12.sp, color = Color(0xFF053f55)),
                                minLines = 3,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF053f55),
                                    unfocusedBorderColor = Color(0xFF053f55).copy(alpha = 0.7f),
                                    cursorColor = Color(0xFF053f55)
                                )
                            )
                        }
                    },
                    confirmButton = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            TextButton(onClick = { showRatingDialog = false }) {
                                Text("Annuler", color = Color.Gray)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            TextButton(
                                onClick = {
                                    ratingViewModel.postRating(rating.toString(), comment)
                                    showRatingDialog = false
                                },
                                enabled = rating > 0
                            ) {
                                Text("Envoyer", color = Color(0xFF053f55), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}
