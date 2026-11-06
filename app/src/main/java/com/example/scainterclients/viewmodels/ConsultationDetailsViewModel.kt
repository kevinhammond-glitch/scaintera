package com.example.scainterclients.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.scainterclients.services.ConsultationDetails
import com.example.scainterclients.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

// Sealed interface to represent the different states of the UI
sealed interface ConsultationDetailsUiState {
    data class Success(val details: ConsultationDetails) : ConsultationDetailsUiState
    data class Error(val message: String) : ConsultationDetailsUiState
    object Loading : ConsultationDetailsUiState
}

class ConsultationDetailsViewModel(private val consultationID: Int) : ViewModel() {

    private val _detailsState = MutableStateFlow<ConsultationDetailsUiState>(ConsultationDetailsUiState.Loading)
    val detailsState: StateFlow<ConsultationDetailsUiState> = _detailsState.asStateFlow()

    init {
        fetchConsultationDetails()
    }

    private fun fetchConsultationDetails() {
        viewModelScope.launch {
            // Set the state to Loading before making the network call
            _detailsState.value = ConsultationDetailsUiState.Loading
            try {
                // Fetch details using the Retrofit instance and the provided consultationID
                val response = RetrofitInstance.apiInterface.getConsultationDetails(consultationID)
                if (response.isSuccessful && response.body() != null) {
                    val apiResponseConsultationDetails = response.body()!!
                    if (apiResponseConsultationDetails.success) {
                        _detailsState.value = ConsultationDetailsUiState.Success(
                            apiResponseConsultationDetails.data)
                    } else {
                        _detailsState.value = ConsultationDetailsUiState.Error("Failed to fetch consultation details: API reported failure.")
                    }
                } else {
                    // If the response is not successful, update the state with an error message
                    _detailsState.value = ConsultationDetailsUiState.Error("Failed to load details: ${response.message()}")
                }
            } catch (e: Exception) {
                // In case of a network or parsing exception, update the state with an error
                _detailsState.value = ConsultationDetailsUiState.Error("An error occurred: ${e.message}")
            }
        }
    }
}

// ViewModel Factory to pass consultationID
class ConsultationDetailsViewModelFactory(private val consultationID: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConsultationDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ConsultationDetailsViewModel(consultationID) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}