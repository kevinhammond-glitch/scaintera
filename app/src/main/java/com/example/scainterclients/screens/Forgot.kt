package com.example.scainterclients.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*

@Composable
fun Forgot() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE7EDEF)),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                text = "Forgot Password Screen",
                Modifier.padding(10.dp),
                color = Color.Black
            )
            Text(text = "Forgot password", Modifier.padding(10.dp), color = Color.Black)
        }
    }
}