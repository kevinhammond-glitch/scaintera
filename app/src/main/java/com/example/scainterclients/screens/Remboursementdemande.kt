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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowCircleLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.scainterclients.RetrofitInstance
import com.example.scainterclients.services.ApiRembRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Remboursementdemande(navController: NavController) {

    val context = androidx.compose.ui.platform.LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val sharedPref = remember { context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE) }

    val storedAssureID = remember { sharedPref.getInt("assureID", 0) }

    var typeRemboursement by remember { mutableStateOf("") }
    var dateRemboursement by remember { mutableStateOf("") }
    var montantDemande by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Consultation", "Dentaire", "Examen", "Hospitalisation", "Optique", "Pharmacie", "International")

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color(0xFF053f55),
        focusedLabelColor = Color(0xFF053f55),
        cursorColor = Color(0xFF053f55),
        unfocusedBorderColor = Color.Black,
        unfocusedLabelColor = Color.Black
    )
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
                text = "Remboursement:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            )
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(10.dp))
                .padding(start = 18.dp, top = 12.dp, bottom = 14.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Demande de remboursement:",
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = typeRemboursement,
                    onValueChange = {},
                    readOnly = true, // User must pick from list
                    label = { Text("Type de remboursement", fontSize = 10.sp) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor() // Important for positioning the menu
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = textFieldColors
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption, fontSize = 10.sp) },
                            onClick = {
                                typeRemboursement = selectionOption
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }
            OutlinedTextField(
                value = montantDemande,
                onValueChange = { montantDemande = it },
                label = { Text("Montant à rembourser", fontSize = 10.sp) },
                placeholder = { Text("0.00", fontSize = 10.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                suffix = { Text("FCFA", fontSize = 10.sp, color = Color(0xFF053f55)) },
                colors = textFieldColors
            )
            OutlinedTextField(
                value = dateRemboursement,
                onValueChange = { dateRemboursement = it },
                label = { Text("Date de prestation", fontSize = 10.sp) },
                placeholder = { Text("JJ/MM/AAAA", fontSize = 10.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = textFieldColors
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    if (typeRemboursement.isNotEmpty() && dateRemboursement.isNotEmpty() && montantDemande.isNotEmpty()) {
                        isLoading = true // 2. Start loading
                        scope.launch {
                            try {
                                val parts = dateRemboursement.split("/")
                                val formattedDate = if (parts.size == 3) {
                                    "${parts[2]}-${parts[1]}-${parts[0]}" // Becomes "2023-12-25"
                                } else {
                                    dateRemboursement // Fallback if format is wrong
                                }

                                val data = ApiRembRequest(
                                    assureID = storedAssureID,
                                    type = typeRemboursement,
                                    montant = montantDemande,
                                    societe = montantDemande,
                                    exclusions = "0",
                                    date = formattedDate,
                                    etat = "En attente de validation",
                                    prestaname = "Etablissement Hors-Réseaux My SCA Clients",
                                    prestataireID = "1366",
                                    genrepresta = "Autres..."
                                )
                                if (dateRemboursement.split("/").size != 3) {
                                    android.widget.Toast.makeText(context, "Format invalide. Utilisez JJ/MM/AAAA", android.widget.Toast.LENGTH_SHORT).show()
                                    isLoading = false // Remember to reset loading!
                                    return@launch
                                }
                                val response = RetrofitInstance.apiInterface.submitData(data)
                                if (response.isSuccessful) {
                                    typeRemboursement = ""
                                    dateRemboursement = ""
                                    montantDemande = ""

                                    android.widget.Toast.makeText(context, "Remboursement soumis avec succès!", android.widget.Toast.LENGTH_SHORT).show()

                                    navController.navigate("remboursement") {
                                        popUpTo("remboursementdemande") { inclusive = true }
                                    }
                                }
                            }  catch (e: Exception) {
                                e.printStackTrace()
                                withContext(Dispatchers.Main) {
                                    android.widget.Toast.makeText(context, "Erreur: ${e.message}", android.widget.Toast.LENGTH_LONG).show()
                                }
                            }
                            finally {
                                isLoading = false // 3. Stop loading regardless of success or failure
                            }
                        }
                    }
                    else {
                        android.widget.Toast.makeText(context, "Veuillez remplir tous les champs", android.widget.Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .height(80.dp)
                    .width(400.dp)
                    .padding(12.dp),
                enabled = !isLoading, // Disable button while loading
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF053f55),
                    disabledContainerColor = Color(0xFF053f55)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                // 4. Conditional UI: Show Spinner or Text
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp), // Set a specific size for the indicator
                        color = Color.White,
                        trackColor = Color.Gray
                    )
                } else {
                    Text("Soumettre la demande", fontSize = 14.sp, color = Color.White)
                }
            }
        }
    }
}
