package com.example.scainterclients.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.scainterclients.services.ConsultationDetails
import com.example.scainterclients.viewmodels.ConsultationDetailsUiState
import com.example.scainterclients.viewmodels.ConsultationDetailsViewModel
import com.example.scainterclients.viewmodels.ConsultationDetailsViewModelFactory
import com.example.scainterclients.viewmodels.RatingConsultViewModel
import com.example.scainterclients.viewmodels.RatingConsultViewModelFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Composable
fun ConsultationDetailScreen(
    consultationID: Int,
    onBackClick: () -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // 1. Initialize ViewModels using Factories to avoid "Cannot create instance" crash
    val detailsViewModel = viewModel<ConsultationDetailsViewModel>(
        factory = ConsultationDetailsViewModelFactory(consultationID)
    )
    val uiState by detailsViewModel.detailsState.collectAsState()

    val ratingViewModel: RatingConsultViewModel = viewModel(
        factory = RatingConsultViewModelFactory(consultationID)
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
        bottomBar = { AppBottomNavigationBar(navController = navController) } // <-- Pass it here
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
                is ConsultationDetailsUiState.Loading -> {
                    // Show a loading indicator
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is ConsultationDetailsUiState.Success -> {
                    // Display the details content on success
                    DetailsContent(
                        details = state.details,
                        ratingViewModel = ratingViewModel
                    )
                }
                is ConsultationDetailsUiState.Error -> {
                    // Show an error message
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
private fun AppBottomNavigationBar(navController: NavHostController)
{
    NavigationBar(
        containerColor = Color(0xFFE7EDEF),
    ) {
        NavigationBarItem(
            icon = { Icon(ImageVector.vectorResource(id = R.drawable.home), contentDescription = "Home") },
            label = { Text("Home", fontSize = 10.sp, fontWeight = FontWeight.Normal) },
            selected = false,
            onClick = { navController.navigate("home") }
        )
        NavigationBarItem(
            icon = { Icon(ImageVector.vectorResource(id = R.drawable.stethoscope), contentDescription = "Consultations") },
            label = { Text("Consult...", fontSize = 10.sp, fontWeight = FontWeight.Normal )},
            selected = true, // Example: Mark this screen as selected
            onClick = {
                navController.navigate("home?start_tab=consultations") {
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
            selected = false,
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
private fun DetailsContent(
    details: ConsultationDetails,
    modifier: Modifier = Modifier,
    ratingViewModel: RatingConsultViewModel
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
            text = "Couverture: ${details.fraisexternes}",
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
                    painter = painterResource(id = R.drawable.stethoscope),
                    contentDescription = "Consultation Icon",
                    tint = Color(0xFF053f55),
                    modifier = Modifier.size(25.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Consultation",
                    lineHeight = 16.sp,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Acte:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 6.dp), fontSize = 12.sp, lineHeight = 16.sp)
            Text(
                text = details.acte ?: "",
                color = Color.Red,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                modifier = Modifier.padding(start = 6.dp),
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Médecin prescripteur:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 6.dp), fontSize = 12.sp, lineHeight = 16.sp)
            Text(
                text = details.doctor ?: "",
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = Color(0xFF053f55),
                modifier = Modifier.padding(start = 6.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Prestataire:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 6.dp), fontSize = 12.sp, lineHeight = 16.sp)
            Text(
                text = details.prestaname ?: "",
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = Color(0xFF053f55),
                modifier = Modifier.padding(start = 6.dp),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Date:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 6.dp), fontSize = 12.sp, lineHeight = 16.sp)
            Text(
                text = formatDisplayDate(details.dateCreated),
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = Color(0xFF053f55),
                modifier = Modifier.padding(start = 6.dp),
            )

            val submissionStatus by ratingViewModel.submissionStatus.collectAsState()
            val ratingViewModel: RatingConsultViewModel = viewModel()

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
        Spacer(modifier = Modifier.height(13.dp))
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(10.dp))
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Assignment,
                    contentDescription = "Medication Icon",
                    tint = Color(0xFF053f55),
                    modifier = Modifier.size(25.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Prescription médicale",
                    lineHeight = 16.sp,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            if (details.quantite != "0") {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if ((details.medicament?.length ?: 0) > 15)
                            "${details.medicament?.take(15)}..."
                        else
                            (details.medicament ?: ""),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        lineHeight = 8.sp
                    )
                    Text(text = details.quantite ?: "", fontWeight = FontWeight.Bold, fontSize = 12.sp, lineHeight = 12.sp)
                }
                Column(modifier = modifier.fillMaxWidth().padding(start = 6.dp)) {
                    Text(text = details.posologie ?: "", fontStyle = FontStyle.Italic, color = Color(0xFF053f55), fontSize = 10.sp, lineHeight = 16.sp)
                }
            }
            else {
                Spacer(modifier = Modifier.height(4.dp))
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 6.dp)
                ) {
                    Text(text = "Aucune prescription médicale", fontStyle = FontStyle.Italic, color = Color(0xFF053f55), fontSize = 10.sp, lineHeight = 16.sp)
                }
            }


            if (details.quantite1 != "0") {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if ((details.medicament1?.length ?: 0) >15)
                            "${details.medicament1?.take(15)}..."
                        else
                            (details.medicament1 ?: ""),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        lineHeight = 8.sp
                    )
                    Text(text = details.quantite1 ?: "", fontWeight = FontWeight.Bold, fontSize = 12.sp, lineHeight = 12.sp)
                }
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 6.dp)
                ) {
                    Text(text = details.posologie1 ?: "", fontStyle = FontStyle.Italic, color = Color(0xFF053f55), fontSize = 10.sp, lineHeight = 16.sp)
                }
            }

            if (details.quantite2 != "0") {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if ((details.medicament2?.length ?: 0) >15)
                            "${details.medicament2?.take(15)}..."
                        else
                            (details.medicament2 ?: ""),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        lineHeight = 8.sp
                    )
                    Text(text = details.quantite2 ?: "", fontWeight = FontWeight.Bold, fontSize = 12.sp, lineHeight = 12.sp)
                }
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 6.dp)
                ) {
                    Text(text = details.posologie2 ?: "", fontStyle = FontStyle.Italic, color = Color(0xFF053f55), fontSize = 10.sp, lineHeight = 16.sp)
                }
            }

            if (details.quantite3 != "0") {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if ((details.medicament3?.length ?: 0) >15)
                            "${details.medicament3?.take(15)}..."
                        else
                            (details.medicament3 ?: ""),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        lineHeight = 8.sp
                    )
                    Text(text = details.quantite3 ?: "", fontWeight = FontWeight.Bold, fontSize = 12.sp, lineHeight = 12.sp)
                }
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 6.dp)
                ) {
                    Text(text = details.posologie3 ?: "", fontStyle = FontStyle.Italic, color = Color(0xFF053f55), fontSize = 10.sp, lineHeight = 16.sp)
                }
            }

        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

