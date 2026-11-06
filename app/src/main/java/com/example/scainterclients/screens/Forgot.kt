package com.example.scainterclients.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.outlined.ArrowCircleLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavController

@Composable
fun Forgot(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "Mot de passe oublié",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd)
            {
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
        Column (
            modifier = Modifier
                .fillMaxWidth() // Make the Row take the full width
                .background(Color.White, shape = RoundedCornerShape(10.dp))
                .padding(start = 18.dp, top = 12.dp, bottom = 14.dp, end = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ){
            Text(
                text = "Pour vos accès, veuillez contacter le Dépt. informatique de SCA INTER A aux numéros suivants:",
                fontSize = 12.sp,
                textAlign = TextAlign.Justify,
                lineHeight = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.HeadsetMic, // Changed to HeadsetMic icon
                    contentDescription = "Headset Icon", // Updated content description
                    modifier = Modifier.padding(end = 4.dp),
                    tint = Color(0xFF053f55)
                )
                Text(
                    text = "(+225) 01 71 9090 32.",
                    fontSize = 12.sp,
                    lineHeight = 20.sp,
                    color = Color(0xFF053f55),
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.HeadsetMic, // Changed to HeadsetMic icon
                    contentDescription = "Headset Icon", // Updated content description
                    modifier = Modifier.padding(end = 4.dp),
                    tint = Color(0xFF053f55)
                )
                Text(
                    text = "(+225) 01 71 9090 34.",
                    fontSize = 12.sp,
                    lineHeight = 20.sp,
                    color = Color(0xFF053f55),
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.HeadsetMic, // Changed to HeadsetMic icon
                    contentDescription = "Headset Icon", // Updated content description
                    modifier = Modifier.padding(end = 4.dp),
                    tint = Color(0xFF053f55)
                )
                Text(
                    text = "(+225) 01 71 9191 05.",
                    fontSize = 12.sp,
                    lineHeight = 20.sp,
                    color = Color(0xFF053f55),
                )
            }
        }
    }
}
