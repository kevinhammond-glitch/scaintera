package com.example.scainterclients.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.scainterclients.services.ExamenDetails
import com.example.scainterclients.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

// Sealed interface to represent the different states of the UI
sealed interface ExamenDetailsUiState {
    data class Success(val details: ExamenDetails) : ExamenDetailsUiState
    data class Error(val message: String) : ExamenDetailsUiState
    object Loading : ExamenDetailsUiState
}

class ExamenDetailsViewModel(private val examenID: Int) : ViewModel() {

    // StateFlow to hold the UI state (Loading, Success, or Error)
    private val _detailsState = MutableStateFlow<ExamenDetailsUiState>(ExamenDetailsUiState.Loading)
    val detailsState: StateFlow<ExamenDetailsUiState> = _detailsState

    // The init block is executed when the ViewModel is created
    init {
        fetchExamenDetails()
    }

    private fun fetchExamenDetails() {
        viewModelScope.launch {
            // Set the state to Loading before making the network call
            _detailsState.value = ExamenDetailsUiState.Loading
            try {
                // Fetch details using the Retrofit instance and the provided ExamenID
                val response = RetrofitInstance.apiInterface.getExamenDetails(examenID)
                if (response.isSuccessful && response.body() != null) {
                    val apiResponseExamenDetails = response.body()!!
                    if (apiResponseExamenDetails.success) {
                        _detailsState.value = ExamenDetailsUiState.Success(
                            apiResponseExamenDetails.data)
                    } else {
                        _detailsState.value = ExamenDetailsUiState.Error("Failed to fetch Examen details: API reported failure.")
                    }
                } else {
                    // If the response is not successful, update the state with an error message
                    _detailsState.value = ExamenDetailsUiState.Error("Failed to load details: ${response.message()}")
                }
            } catch (e: Exception) {
                // In case of a network or parsing exception, update the state with an error
                _detailsState.value = ExamenDetailsUiState.Error("An error occurred: ${e.message}")
            }
        }
    }
}

// ViewModel Factory to pass ExamenID
class ExamenDetailsViewModelFactory(private val examenID: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExamenDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExamenDetailsViewModel(examenID) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}