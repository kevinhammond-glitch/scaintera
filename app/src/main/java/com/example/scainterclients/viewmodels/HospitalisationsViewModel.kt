package com.example.scainterclients.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.scainterclients.services.Hospit
import com.example.scainterclients.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// UI State sealed interface for Hospits
sealed interface HospitsUiState {
    data class Success(val hospits: List<Hospit>) : HospitsUiState
    data class Error(val message: String) : HospitsUiState
    object Loading : HospitsUiState
}

class HospitsViewModel(private val assureID: Int) : ViewModel() {

    private val _hospitsState = MutableStateFlow<HospitsUiState>(HospitsUiState.Loading)
    val hospitsState: StateFlow<HospitsUiState> = _hospitsState.asStateFlow()

    init {
        fetchHospits()
    }

    private fun fetchHospits() {
        viewModelScope.launch {
            _hospitsState.value = HospitsUiState.Loading
            try {
                val response = RetrofitInstance.apiInterface.getHospits(assureID)
                if (response.isSuccessful && response.body() != null) {
                    val apiResponseHospit = response.body()!!
                    if (apiResponseHospit.success) {
                        _hospitsState.value = HospitsUiState.Success(
                            apiResponseHospit.data)
                    } else {
                        _hospitsState.value = HospitsUiState.Error("Failed to fetch hospits: API reported failure.")
                    }
                } else {
                    _hospitsState.value = HospitsUiState.Error("Failed to fetch hospits: ${response.message()}")
                }
            } catch (e: Exception) {
                _hospitsState.value = HospitsUiState.Error("An error occurred: ${e.message}")
            }
        }
    }
}

// ViewModel Factory to pass assureID
class HospitsViewModelFactory(private val assureID: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HospitsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HospitsViewModel(assureID) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
