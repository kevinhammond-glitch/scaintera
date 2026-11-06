package com.example.scainterclients.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.scainterclients.services.Consultation
import com.example.scainterclients.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// UI State sealed interface for Consultations
sealed interface ConsultationsUiState {
    data class Success(val consultations: List<Consultation>) : ConsultationsUiState
    data class Error(val message: String) : ConsultationsUiState
    object Loading : ConsultationsUiState
}

class ConsultationsViewModel(private val assureID: Int) : ViewModel() {

    private val _consultationsState = MutableStateFlow<ConsultationsUiState>(ConsultationsUiState.Loading)
    val consultationsState: StateFlow<ConsultationsUiState> = _consultationsState.asStateFlow()

    init {
        fetchConsultations()
    }

    private fun fetchConsultations() {
        viewModelScope.launch {
            _consultationsState.value = ConsultationsUiState.Loading
            try {
                val response = RetrofitInstance.apiInterface.getConsultations(assureID)
                if (response.isSuccessful && response.body() != null) {
                    val apiResponseConsultation = response.body()!!
                    if (apiResponseConsultation.success) {
                        _consultationsState.value = ConsultationsUiState.Success(
                            apiResponseConsultation.data)
                    } else {
                        _consultationsState.value = ConsultationsUiState.Error("Failed to fetch consultations: API reported failure.")
                    }
                } else {
                    _consultationsState.value = ConsultationsUiState.Error("Failed to fetch consultations: ${response.message()}")
                }
            } catch (e: Exception) {
                _consultationsState.value = ConsultationsUiState.Error("An error occurred: ${e.message}")
            }
        }
    }
}

// ViewModel Factory to pass assureID
class ConsultationsViewModelFactory(private val assureID: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConsultationsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ConsultationsViewModel(assureID) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
