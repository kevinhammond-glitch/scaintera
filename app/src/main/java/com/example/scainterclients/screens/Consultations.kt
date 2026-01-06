package com.example.scainterclients.screens

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 1. Data class for an Examination
data class Consultation(
    val id: Int,
    val ExamName: String,
    val status: String,
    val date: String
)

// 2. Sample data for examinations
val sampleConsultations = listOf(
    Examination(1, "Blood Test", "Accord des Médecins", "2024-11-18"),
    Examination(2, "X-Ray - Chest", "En attente de validation", "2024-11-21"),
    Examination(3, "MRI - Head", "Accord des Médecins", "2024-11-25"),
    Examination(4, "Ultrasound", "Accord des Médecins", "2024-12-03"),
    Examination(5, "ECG", "Rejeté", "2024-12-08")
)


@Composable
fun Consultations(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "My Examinations",
            fontSize = 22.sp,
            lineHeight = 36.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(bottom = 8.dp) // Space below title
        )

        // 3. Use LazyColumn for an efficient list
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp) // Space between items
        ) {
            items(sampleExaminations) { examination ->
                ConsultationItem(examination = examination)
            }
        }
    }
}

// 4. Composable for a single examination item
@Composable
fun ConsultationItem(examination: Examination, modifier: Modifier = Modifier) {
    ListItem(
        headlineContent = { Text(text = examination.ExamName, fontWeight = FontWeight.Bold) },
        supportingContent = { Text(text = "Statut: ${examination.status}") },
        trailingContent = { Text(text = examination.date) },
        leadingContent = {
            Icon(
                imageVector = Icons.Default.MonitorHeart,
                contentDescription = "Examination Icon"
            )
        },
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(4.dp)
            )
            .clip(RoundedCornerShape(4.dp))
    )
    Spacer(modifier = Modifier.height(4.dp))
}

