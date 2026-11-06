package com.example.scainterclients.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scainterclients.R
import com.example.scainterclients.services.Hospit
import com.example.scainterclients.viewmodels.HospitsUiState
import com.example.scainterclients.viewmodels.HospitsViewModel
import com.example.scainterclients.viewmodels.HospitsViewModelFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Composable
fun Hospitalisations(assureID: Int, onHospitClick: (Int) -> Unit, modifier: Modifier = Modifier) {
     val viewModel: HospitsViewModel = viewModel(
        factory = HospitsViewModelFactory(assureID)
    )

    val uiState by viewModel.hospitsState.collectAsState()

    when (val state = uiState) {
        is HospitsUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is HospitsUiState.Success -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(start = 10.dp, end = 10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp) // Space between items
            ) {
                item {
                    Text(
                        text = "Mes hospitalisations",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }

                // --- List of Hospitalisations ---
                items(state.hospits) { hospit ->
                    HospitalisationItem(
                        hospit = hospit,
                        onDetailsClick = { onHospitClick(hospit.hospitID) }
                    )
                }
            }
        }
        is HospitsUiState.Error -> {
            // Show a centered error message
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

private fun formatDisplayDate(dateString: String): String {
    // Formatter for "yyyy-MM-dd HH:mm:ss"
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
            // If all parsing fails, return just the date part of the original string
            dateString.substringBefore("T").substringBefore(" ")
        }
    }
}

@Composable
fun HospitalisationItem(hospit: Hospit, onDetailsClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(10.dp)
            )
            .clip(RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.hotel),
            contentDescription = "Hospitalisation Icon",
            tint = Color(0xFF053f55),
            modifier = Modifier.size(25.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = if (hospit.fullname.length > 22) "${hospit.fullname.take(22)}..." else hospit.fullname,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = Color(0xFF053f55),
                fontWeight = FontWeight.Bold
            )
            Text(text = hospit.statut, color = Color.Red, fontSize = 12.sp, lineHeight = 16.sp, fontStyle = FontStyle.Italic)
            Text(text = if (hospit.prestaname.length > 22) "${hospit.prestaname.take(22)}..." else hospit.prestaname, fontSize = 12.sp, color = Color(0xFF053f55), lineHeight = 16.sp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatDisplayDate(hospit.dateCreated),
                    fontSize = 12.sp,
                    color = Color(0xFF053f55)
                )
                CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                    TextButton(
                        onClick = onDetailsClick,
                        contentPadding = PaddingValues(0.dp), // Removes internal padding
                        modifier = Modifier.defaultMinSize(minHeight = 1.dp, minWidth = 1.dp) // Removes default min constraints
                    ) {
                        Text(
                            "Détails",
                            color = Color(0xFF053f55),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold // Optional: adds visibility since it's smaller now
                        )
                    }
                }
            }
        }
    }
}
