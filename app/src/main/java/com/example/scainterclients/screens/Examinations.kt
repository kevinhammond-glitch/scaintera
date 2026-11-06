package com.example.scainterclients.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.scainterclients.services.Examen
import com.example.scainterclients.viewmodels.ExamensUiState
import com.example.scainterclients.viewmodels.ExamensViewModel
import com.example.scainterclients.viewmodels.ExamensViewModelFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Composable
fun Examinations(assureID: Int, onExamenClick: (Int) -> Unit, modifier: Modifier = Modifier) {
    // 1. Initialize the ViewModel with the factory to pass the familleID
    val viewModel: ExamensViewModel = viewModel(
        factory = ExamensViewModelFactory(assureID)
    )
    // 2. Collect the UI state from the ViewModel
    val uiState by viewModel.examensState.collectAsState()

    // 3. Display content based on the current UI state
    when (val state = uiState) {
        is ExamensUiState.Loading -> {
            // Show a loading indicator in the center
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is ExamensUiState.Success -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(start = 10.dp, end = 10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp) // Space between items
            ) {
                 item {
                    Text(
                        text = "Mes examens",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }

                // --- List of Examinations ---
                items(state.examens) { examen ->
                    ExaminationItem(
                        examen = examen,
                        onDetailsClick = { onExamenClick(examen.examenID) }
                    )
                }
            }
        }
        is ExamensUiState.Error -> {
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
fun ExaminationItem(examen: Examen, onDetailsClick: () -> Unit, modifier: Modifier = Modifier) {
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
            ImageVector.vectorResource(id = R.drawable.vitalsigns),
            contentDescription = "Examination Icon",
            tint = Color(0xFF053f55),
            modifier = Modifier.size(25.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = if (examen.fullname.length > 22) "${examen.fullname.take(22)}..." else examen.fullname,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = Color(0xFF053f55),
                fontWeight = FontWeight.Bold
            )
            Text(text = examen.statut, color = Color.Red, fontSize = 12.sp, lineHeight = 16.sp, fontStyle = FontStyle.Italic)
            Text(text = if (examen.prestaname.length > 22) "${examen.prestaname.take(22)}..." else examen.prestaname, fontSize = 12.sp, color = Color(0xFF053f55), lineHeight = 16.sp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatDisplayDate(examen.dateCreated),
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
