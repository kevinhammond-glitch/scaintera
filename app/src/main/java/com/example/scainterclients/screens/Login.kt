package com.example.scainterclients.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.scainterclients.R
import com.example.scainterclients.viewmodels.LoginViewModel
import com.example.scainterclients.viewmodels.LoginUiState
import android.widget.Toast

@Composable
fun Login(navController: NavHostController, modifier: Modifier = Modifier,
          loginViewModel: LoginViewModel = viewModel() ) {
    // our image resource file.
    val painter = painterResource(id = R.drawable.mobileapplogo)
    // Our logo text
    val sca = "SCA"
    val intera = "INTER A"
    // Our welcome text
    val message = "Welcome to mySCA Inter"
    val from = "Client"
    // Remember the text entered in the TextField
    var username by remember { mutableStateOf("") }
    // Create a mutable state for the password text
    var password by remember { mutableStateOf("") }
    // Create a mutable state for the password visibility
    var passwordVisible by remember { mutableStateOf(false) }
    // Remember the switch button state and forgot password
    val mContext = LocalContext.current
    val mCheckedState = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Collect the login state from the ViewModel
    val loginState by loginViewModel.loginState.collectAsState()

    // This effect runs when the loginState changes
    LaunchedEffect(loginState) {
        when (val state = loginState) {
            is LoginUiState.Success -> {

                //Toast.makeText(mContext, "Login Successful! FamilleID: $familleID and Fullname: $fullname", Toast.LENGTH_SHORT).show()

                // Navigation to the Home screen with the familleID as an argument
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }
            is LoginUiState.Error -> {
                // Show a detailed error message from the ViewModel, with a fallback message
                val errorMessage = state.message.ifBlank { "An unknown error occurred from Login" }
                Toast.makeText(mContext, "Login Failed: $errorMessage", Toast.LENGTH_LONG).show()
            }
            else -> {
                // You can log or handle Idle and Loading states if needed
            }
        }
    }
    Column(
        modifier = modifier.fillMaxSize(), // Added fillMaxSize to better center content
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally // Center children horizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(), // Make the Row take the full width
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically // Pushes texts to the start and end
        ) {
            // below is the composable for image.
            Image(
                painter = painter,
                contentDescription = "SCA Inter A logo",
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .padding(end = 16.dp),
                contentScale = androidx.compose.ui.layout.ContentScale.Fit
            )
            Text(
                text = sca,
                modifier = Modifier.padding(end = 8.dp),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF053f55)
            )
            Text(
                text = intera,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(26.dp))
        // Text are now outside the Image
        Text(
            text = message,
            fontSize = 58.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 72.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF053f55)
        )
        // Wrap the Text and Spacer in a Row to position them horizontally
        Row(
            modifier = Modifier
                .fillMaxWidth() // Make the Row take the full width
                .padding(end = 20.dp), // Apply padding to the end of the Row
            horizontalArrangement = Arrangement.End, // Align children to the end (right)
            verticalAlignment = Alignment.CenterVertically // Vertically center the items in the Row
        ) {
            Text(
                text = from,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF053f55)
            )
        }
        Column(
            modifier = Modifier
                .width(400.dp) // Let the content determine the height
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp), // Adds space between items
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // TextField for user input (Username)
            TextField(
                value = username,
                onValueChange = { username = it },
                placeholder = { Text("Enter your username") },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Filled.AccountCircle, contentDescription = null, tint = Color(0xFF053f55))
                },
                colors = TextFieldDefaults.colors(
                    focusedLabelColor = Color(0xFF053f55),
                    unfocusedLabelColor = Color(0xFF053f55)
                ),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
            // TextField for giving password input
            TextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Password") },
                singleLine = true,
                placeholder = { Text("Password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description = if (passwordVisible) "Hide password" else "Show password"
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = description)
                    }
                },
                leadingIcon = {
                    Icon(Icons.Filled.Lock, contentDescription = null, tint = Color(0xFF053f55))
                },
                colors = TextFieldDefaults.colors(
                    focusedLabelColor = Color(0xFF053f55),
                    unfocusedLabelColor = Color(0xFF053f55)
                ),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // "Remember me" switch and label
            Row(
                verticalAlignment = Alignment.CenterVertically,
                //modifier = Modifier.clickable { // Make the entire label area clickable to toggle the switch
                //    mCheckedState.value = !mCheckedState.value
                //}
            ) {
                Switch(
                    checked = mCheckedState.value,
                    onCheckedChange = { mCheckedState.value = it }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Remember me")
            }
            // "Forgot password?" text link
            Text(
                text = "Forgot password?",
                color = Color(0xFF053f55),
                textDecoration = TextDecoration.Underline,
                //modifier = Modifier.clickable { navController.navigate("forgot") } // Correct way to handle clicks
            )

        }
        Button(
            modifier = Modifier
                .height(100.dp)
                .width(400.dp)
                .padding(16.dp),
            onClick = {
                // Call the ViewModel function on button click
                loginViewModel.loginUser(username, password)
            },
            // Disable the button while the login request is in progress
            enabled = loginState != LoginUiState.Loading,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF053f55),
                disabledContainerColor = Color(0xFF053f55)
            )
        ) {
            if (loginState == LoginUiState.Loading) {
                // Show a progress indicator when loading
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp), // Set a specific size for the indicator
                    color = Color.White,
                    trackColor = Color.Gray
                )
            } else {
                Text(
                    stringResource(R.string.roll),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}
