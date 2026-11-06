package com.example.scainterclients.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scainterclients.RetrofitInstance
import com.example.scainterclients.services.LoginRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.String

// Sealed interface to represent the different states of the Login UI
sealed interface LoginUiState {
    object Idle : LoginUiState
    object Loading : LoginUiState
    data class Success(
        val familleID: Int,
        val assureID: Int,
        val fullname: String,
        val statut: String,
        val reseaux: String,
        val affiliation: String,
        val souscripteur: String,
        val fraisexternes: String,
        val hospitalisation: String) : LoginUiState

    data class Error(val message: String) : LoginUiState
}

class LoginViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState.asStateFlow()
    fun logout() {
        _loginState.value = LoginUiState.Idle
    }
    fun loginUser(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginUiState.Loading
            try {
                // 1. Create an instance of LoginRequest
                val request = LoginRequest(username = username, password = password)

                // 2. Pass the request object to the API call
                val response = RetrofitInstance.apiInterface.fetch_assure_post(request)

                if (response.isSuccessful) {
                    val loginResponse = response.body() // This is your LoginResponse object
                    if (loginResponse != null) {
                        Log.d("API_SUCCESS",
                            "Famille ID: ${loginResponse.familleID}")
                        // **FIXED:** Pass all required properties from loginResponse to the Success state
                        _loginState.value = LoginUiState.Success(
                            familleID = loginResponse.familleID,
                            assureID = loginResponse.assureID,
                            fullname = loginResponse.fullname,
                            statut = loginResponse.statut,
                            reseaux = loginResponse.reseaux,
                            affiliation = loginResponse.affiliation,
                            souscripteur = loginResponse.souscripteur,
                            fraisexternes = loginResponse.fraisexternes,
                            hospitalisation = loginResponse.hospitalisation
                        )

                    } else {
                        Log.e("API_ERROR", "Response body is null even on success")
                        _loginState.value = LoginUiState.Error("Received an empty successful response.")
                    }
                } else {
                    // Read the error body only once.
                    val errorBodyString = response.errorBody()?.string()

                    if (errorBodyString.isNullOrEmpty()) {
                        _loginState.value = LoginUiState.Error("An unknown error occurred from View.")
                    } else {
                        // Optionally, parse the error string if it's a JSON
                        // This prevents sending raw HTML or unformatted text to the UI
                        val parsedMessage = try {
                            val jsonObject = JSONObject(errorBodyString)
                            // Assuming the server sends {"message": "your_error"}
                            jsonObject.optString("message", "Failed to parse error message.")
                        } catch (e: Exception) {
                            // If parsing fails, use the raw string as a fallback
                            errorBodyString
                        }
                        _loginState.value = LoginUiState.Error(parsedMessage)
                    }
                }
            } catch (e: Exception) {
                Log.e("API_EXCEPTION", "Exception during login", e)
                // Handle exceptions like network issues
                _loginState.value = LoginUiState.Error("Could not connect to the server. Please check your internet connection.")
            }
        }
    }
}
