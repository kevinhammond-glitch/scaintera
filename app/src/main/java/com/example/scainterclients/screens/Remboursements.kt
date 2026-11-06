package com.example.scainterclients.screens

import android.content.Context
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowCircleLeft
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.scainterclients.R
import com.example.scainterclients.services.RembDetails
import com.example.scainterclients.viewmodels.RembDetailsUiState
import com.example.scainterclients.viewmodels.RembDetailsViewModel
import com.example.scainterclients.viewmodels.RembDetailsViewModelFactory
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Remboursement(navController: NavController) {

    val context = androidx.compose.ui.platform.LocalContext.current
    val scrollState = rememberScrollState()

    val sharedPref = remember { context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE) }

    val storedAssureID = remember { sharedPref.getInt("assureID", 0) }

    val viewModel: RembDetailsViewModel = viewModel(
        factory = RembDetailsViewModelFactory(storedAssureID)
    )

    val uiState by viewModel.detailsState.collectAsState()

    // Fetch data when the screen loads
    LaunchedEffect(storedAssureID) {
        if (storedAssureID != 0) {
            viewModel.fetchRembDetails(storedAssureID)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 6.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "Mes remboursements:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { navController.navigate("remboursementdemande") }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.add_circle),
                    contentDescription = "Ajouter une demande",
                    tint = Color(0xFF053f55),
                    modifier = Modifier.size(30.dp)
                )
            }
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowCircleLeft,
                        contentDescription = "Back",
                        tint = Color(0xFF053f55),
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        when (val state = uiState) {
            is RembDetailsUiState.Loading -> {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF053f55))
                }
            }
            is RembDetailsUiState.Success -> {
                if (state.details.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, shape = RoundedCornerShape(10.dp))
                            .padding(start = 10.dp, end = 10.dp, top = 14.dp, bottom = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Aucune demande de remboursement trouvée. " +
                                    "Veuillez appuyer sur le bouton + pour effectuer une demande de remboursement.",
                            color = Color(0xFF053f55),
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                } else {
                    state.details.forEach { details ->
                        RemboursementItem(details)
                        Spacer(modifier = Modifier.height(18.dp))
                    }
                }
            }
            is RembDetailsUiState.Error -> {
                Text(
                    text = state.message,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

private fun formatDisplayDate(dateString: String?): String {
    if (dateString.isNullOrBlank()) return ""

    // Define input formatters
    val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val dateOnlyFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    // Define output format
    val outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    return try {
        // 1. Try ISO Offset (e.g., 2023-10-25T10:00:00+01:00)
        java.time.OffsetDateTime.parse(dateString).format(outputFormatter)
    } catch (e: Exception) {
        try {
            // 2. Try Local Date Time (e.g., 2023-10-25T10:00:00)
            java.time.LocalDateTime.parse(dateString).format(outputFormatter)
        } catch (e: Exception) {
            try {
                // 3. Try custom SQL format (yyyy-MM-dd HH:mm:ss)
                java.time.LocalDateTime.parse(dateString, dateTimeFormatter).format(outputFormatter)
            } catch (e: Exception) {
                try {
                    // 4. Try Date Only (yyyy-MM-dd)
                    java.time.LocalDate.parse(dateString, dateOnlyFormatter).format(outputFormatter)
                } catch (e: Exception) {
                    // Fallback: If it's already JJ/MM/AAAA or unknown, return as is
                    dateString.substringBefore(" ")
                }
            }
        }
    }
}

private fun formatCurrency(amount: String?): String {
    if (amount.isNullOrBlank()) return "0 FCFA"

    return try {
        // Clean the string (remove spaces or existing suffixes if any)
        val cleanAmount = amount.replace(Regex("[^0-9.]"), "")
        val parsed = cleanAmount.toDouble()

        // Format with space as thousand separator
        val symbols = java.text.DecimalFormatSymbols().apply {
            groupingSeparator = ' '
        }
        val formatter = java.text.DecimalFormat("#,###", symbols)

        "${formatter.format(parsed)} FCFA"
    } catch (e: Exception) {
        // If parsing fails, just append FCFA to the raw string
        "$amount FCFA"
    }
}

@Composable
fun RemboursementItem(details: RembDetails, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(10.dp))
            .padding(start = 10.dp, end = 10.dp, top = 14.dp, bottom = 14.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Type de remboursement:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 6.dp), fontSize = 12.sp, lineHeight = 16.sp)
        Text(
            text = details.type,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            color = Color(0xFF053f55),
            modifier = Modifier.padding(start = 6.dp),
            fontStyle = FontStyle.Italic
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Statut:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 6.dp), fontSize = 12.sp, lineHeight = 16.sp)
        Text(
            text = details.etat,
            color = Color.Red,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            modifier = Modifier.padding(start = 6.dp),
            fontStyle = FontStyle.Italic
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Montant à rembourser:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 6.dp), fontSize = 12.sp, lineHeight = 16.sp)
        Text(
            text = formatCurrency(details.montant),
            fontSize = 12.sp,
            lineHeight = 16.sp,
            color = Color(0xFF053f55),
            modifier = Modifier.padding(start = 6.dp),
            fontStyle = FontStyle.Italic
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Montant remboursé:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 6.dp), fontSize = 12.sp, lineHeight = 16.sp)
        Text(
            text = if (details.rembourser == "NULL" || details.rembourser == "0") {
                "Aucun remboursement effectué"
            } else {
                formatCurrency(details.rembourser)
            },
            fontSize = 12.sp,
            lineHeight = 16.sp,
            color = Color(0xFF053f55),
            modifier = Modifier.padding(start = 6.dp),
            fontStyle = FontStyle.Italic
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Date de prestation:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 6.dp), fontSize = 12.sp, lineHeight = 16.sp)
        Text(
            text = formatDisplayDate(details.prestadate),
            fontSize = 12.sp,
            lineHeight = 16.sp,
            color = Color(0xFF053f55),
            modifier = Modifier.padding(start = 6.dp),
            fontStyle = FontStyle.Italic
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Date de saisie:", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 6.dp), fontSize = 12.sp, lineHeight = 16.sp)
        Text(
            text = formatDisplayDate(details.dateCreated),
            fontSize = 12.sp,
            lineHeight = 16.sp,
            color = Color(0xFF053f55),
            modifier = Modifier.padding(start = 6.dp),
            fontStyle = FontStyle.Italic
        )
    }
}