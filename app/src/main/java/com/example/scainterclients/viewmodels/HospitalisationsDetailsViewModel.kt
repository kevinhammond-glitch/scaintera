package com.example.scainterclients.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.scainterclients.services.HospitDetails
import com.example.scainterclients.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

// Sealed interface to represent the different states of the UI
sealed interface HospitDetailsUiState {
    data class Success(val details: HospitDetails) : HospitDetailsUiState
    data class Error(val message: String) : HospitDetailsUiState
    object Loading : HospitDetailsUiState
}

class HospitDetailsViewModel(private val hospitID: Int) : ViewModel() {

    // StateFlow to hold the UI state (Loading, Success, or Error)
    private val _detailsState = MutableStateFlow<HospitDetailsUiState>(HospitDetailsUiState.Loading)
    val detailsState: StateFlow<HospitDetailsUiState> = _detailsState

    // The init block is executed when the ViewModel is created
    init {
        fetchHospitDetails()
    }

    private fun fetchHospitDetails() {
        viewModelScope.launch {
            // Set the state to Loading before making the network call
            _detailsState.value = HospitDetailsUiState.Loading
            try {
                // Fetch details using the Retrofit instance and the provided HospitID
                val response = RetrofitInstance.apiInterface.getHospitDetails(hospitID)
                if (response.isSuccessful && response.body() != null) {
                    val apiResponseHospitDetails = response.body()!!
                    if (apiResponseHospitDetails.success) {
                        _detailsState.value = HospitDetailsUiState.Success(
                            apiResponseHospitDetails.data)
                    } else {
                        _detailsState.value = HospitDetailsUiState.Error("Failed to fetch Hospit details: API reported failure.")
                    }
                } else {
                    // If the response is not successful, update the state with an error message
                    _detailsState.value = HospitDetailsUiState.Error("Failed to load details: ${response.message()}")
                }
            } catch (e: Exception) {
                // In case of a network or parsing exception, update the state with an error
                _detailsState.value = HospitDetailsUiState.Error("An error occurred: ${e.message}")
            }
        }
    }
}

// ViewModel Factory to pass HospitID
class HospitDetailsViewModelFactory(private val hospitID: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HospitDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HospitDetailsViewModel(hospitID) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}