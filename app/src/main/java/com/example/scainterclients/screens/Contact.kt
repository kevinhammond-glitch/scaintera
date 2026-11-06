package com.example.scainterclients.screens

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
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.outlined.ArrowCircleLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun Contact(navController: NavController) {

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "À propos de nous",
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
                text = "My SCA Clients est une application mobile qui permet " +
                        "aux utilisateurs de réaliser des soins de santé " +
                        "à travers un réseau de soins défini par la société SCA INTER A. " +
                        "Cette application est donc un produit du groupe SCA INTER A.",
                fontSize = 12.sp,
                textAlign = TextAlign.Justify,
                lineHeight = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Nos coordonnées: ",
                fontSize = 12.sp,
                textAlign = TextAlign.Justify,
                lineHeight = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically)
            {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Adresse",
                    modifier = Modifier.padding(end = 4.dp),
                    tint = Color(0xFF053f55)
                )
                Text(
                    text = "Cocody, Abidjan, RCI.",
                    fontSize = 12.sp,
                    lineHeight = 20.sp,
                    color = Color(0xFF053f55),
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically)
            {
                Icon(
                    imageVector = Icons.Default.HeadsetMic,
                    contentDescription = "Headset Icon",
                    modifier = Modifier.padding(end = 4.dp),
                    tint = Color(0xFF053f55)
                )
                Text(
                    text = "(+225) 27 22 550 196.",
                    fontSize = 12.sp,
                    lineHeight = 20.sp,
                    color = Color(0xFF053f55),
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically)
            {
                Icon(
                    imageVector = Icons.Default.Mail,
                    contentDescription = "Email",
                    modifier = Modifier.padding(end = 4.dp),
                    tint = Color(0xFF053f55)
                )
                Text(
                    text = "info@scaintera.com",
                    fontSize = 12.sp,
                    lineHeight = 20.sp,
                    color = Color(0xFF053f55),
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically)
            {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = "Web site",
                    modifier = Modifier.padding(end = 4.dp),
                    tint = Color(0xFF053f55)
                )
                Text(
                    text = "www.scaintera.com",
                    fontSize = 12.sp,
                    lineHeight = 20.sp,
                    color = Color(0xFF053f55),
                )
            }
        }
    }
}
