package com.example.scainterclients.screens


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
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
import com.example.scainterclients.data.UserPreferences
import com.example.scainterclients.viewmodels.LoginUiState
import com.example.scainterclients.viewmodels.LoginViewModel

@Composable
fun Login(
    navController: NavHostController, modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = viewModel()
) {
    // our image resource file.
    val painter = painterResource(id = R.drawable.newmobilelogoapp)
    // Our logo text
    val sca = "SCA"
    val intera = "INTER A"
    // App name
    val message = "MySCA Clients"
    // Remember the text entered in the TextField
    var username by remember { mutableStateOf("") }
    // Create a mutable state for the password text
    var password by remember { mutableStateOf("") }
    // Create a mutable state for the password visibility
    var passwordVisible by remember { mutableStateOf(false) }
    // Remember the switch button state and forgot password
    val mContext = LocalContext.current
    val mCheckedState = remember { mutableStateOf(false) }

    // Instantiate UserPreferences
    val userPreferences = remember { UserPreferences(mContext) }

    // Collect the login state from the ViewModel
    val loginState by loginViewModel.loginState.collectAsState()

    // This effect runs once when the composable is first launched
    LaunchedEffect(Unit) {
        val savedUsername = userPreferences.getUsername()
        val savedPassword = userPreferences.getPassword()
        if (savedUsername != null && savedPassword != null) {
            username = savedUsername
            password = savedPassword
            mCheckedState.value = true
        }
    }


    // This effect runs when the loginState changes
    LaunchedEffect(loginState) {
        when (val state = loginState) {
            is LoginUiState.Success -> {

                // Save or clear username based on the switch state
                if (mCheckedState.value) {
                    userPreferences.saveUsername(username)
                    userPreferences.savePassword(password)
                } else {
                    userPreferences.clearUsername()
                    userPreferences.clearPassword()
                }

                userPreferences.saveAssureId(state.assureID)

                // Navigation to the Home screen
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
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painter,
                contentDescription = "SCA Inter A logo",
                modifier = Modifier
                    .width(60.dp)
                    .height(60.dp)
                    .padding(end = 10.dp),
                contentScale = androidx.compose.ui.layout.ContentScale.Fit
            )
            Text(
                text = sca,
                modifier = Modifier.padding(end = 8.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF053f55)
            )
            Text(
                text = intera,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(70.dp))
        Text(
            text = message,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            lineHeight = 50.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF053f55)
        )
        Column(
            modifier = Modifier
                .width(400.dp)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp), // Adds space between items
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = username,
                onValueChange = { username = it },
                placeholder = { Text("Identifiant", fontSize = 10.sp, color = Color(0xFF053f55)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 60.dp),
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Filled.AccountCircle, contentDescription = null, tint = Color(0xFF053f55))
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedLabelColor = Color(0xFF053f55),
                    unfocusedLabelColor = Color(0xFF053f55),
                    focusedIndicatorColor = Color(0xFF053f55),
                    unfocusedIndicatorColor = Color(0xFF053f55)
                ),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 12.sp
                )
            )
            TextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Mot de passe", fontSize = 10.sp, color = Color(0xFF053f55)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 60.dp),
                singleLine = true,
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
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedLabelColor = Color(0xFF053f55),
                    unfocusedLabelColor = Color(0xFF053f55),
                    focusedIndicatorColor = Color(0xFF053f55),
                    unfocusedIndicatorColor = Color(0xFF053f55)
                ),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 12.sp
                )
            )

        }
        Row(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // "Remember me" switch and label
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Switch(
                    checked = mCheckedState.value,
                    onCheckedChange = { mCheckedState.value = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF053f55)
                    )
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Se souvenir de moi", fontSize = 10.sp)
            }
        }
        Spacer(modifier = Modifier.height(80.dp))
        Text(
                text = "Mot de passe oublié?",
        color = Color(0xFF053f55),
        fontSize = 10.sp,
        textDecoration = TextDecoration.Underline,
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        navController.navigate("forgot")
                    }
                )
            }
        )
        Button(
            modifier = Modifier
                .height(80.dp)
                .width(400.dp)
                .padding(12.dp),
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
                    fontSize = 14.sp
                )
            }
        }
    }
}
