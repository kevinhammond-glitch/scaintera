package com.example.scainterclients.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.scainterclients.services.RembDetails
import com.example.scainterclients.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

// Sealed interface to represent the different states of the UI
sealed interface RembDetailsUiState {
    data class Success(val details: List<RembDetails>) : RembDetailsUiState
    data class Error(val message: String) : RembDetailsUiState
    object Loading : RembDetailsUiState
}

class RembDetailsViewModel(private val assureID: Int) : ViewModel() {

    private val _detailsState = MutableStateFlow<RembDetailsUiState>(RembDetailsUiState.Loading)
    val detailsState: StateFlow<RembDetailsUiState> = _detailsState.asStateFlow()

    init {
        fetchRembDetails(assureID)
    }

    fun fetchRembDetails(assureID: Int) {
        viewModelScope.launch {
            // Set the state to Loading before making the network call
            _detailsState.value = RembDetailsUiState.Loading
            try {
                // Fetch details using the Retrofit instance and the provided assureID
                val response = RetrofitInstance.apiInterface.getRembDetails(assureID)
                if (response.isSuccessful && response.body() != null) {
                    val apiResponseRembDetails = response.body()!!
                    if (apiResponseRembDetails.success) {
                        _detailsState.value = RembDetailsUiState.Success(
                            apiResponseRembDetails.data)
                    } else {
                        _detailsState.value = RembDetailsUiState.Error("Failed to fetch Remb details: API reported failure.")
                    }
                } else {
                    // If the response is not successful, update the state with an error message
                    _detailsState.value = RembDetailsUiState.Error("Failed to load details: ${response.message()}")
                }
            } catch (e: Exception) {
                // In case of a network or parsing exception, update the state with an error
                _detailsState.value = RembDetailsUiState.Error("An error occurred: ${e.message}")
            }
        }
    }
}

// ViewModel Factory to pass assureID
class RembDetailsViewModelFactory(private val assureID: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RembDetailsViewModel::class.java)) {
            // Pass the assureID and your Retrofit API instance to the ViewModel
            return RembDetailsViewModel(assureID) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}