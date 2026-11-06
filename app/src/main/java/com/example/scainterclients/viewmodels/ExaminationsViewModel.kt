package com.example.scainterclients.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.scainterclients.services.Examen
import com.example.scainterclients.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// UI State sealed interface for Examens
sealed interface ExamensUiState {
    data class Success(val examens: List<Examen>) : ExamensUiState
    data class Error(val message: String) : ExamensUiState
    object Loading : ExamensUiState
}

class ExamensViewModel(private val assureID: Int) : ViewModel() {

    private val _examensState = MutableStateFlow<ExamensUiState>(ExamensUiState.Loading)
    val examensState: StateFlow<ExamensUiState> = _examensState.asStateFlow()

    init {
        fetchExamens()
    }

    private fun fetchExamens() {
        viewModelScope.launch {
            _examensState.value = ExamensUiState.Loading
            try {
                val response = RetrofitInstance.apiInterface.getExams(assureID)
                if (response.isSuccessful && response.body() != null) {
                    val apiResponseExamen = response.body()!!
                    if (apiResponseExamen.success) {
                        _examensState.value = ExamensUiState.Success(
                            apiResponseExamen.data)
                    } else {
                        _examensState.value = ExamensUiState.Error("Failed to fetch examens: API reported failure.")
                    }
                } else {
                    _examensState.value = ExamensUiState.Error("Failed to fetch examens: ${response.message()}")
                }
            } catch (e: Exception) {
                _examensState.value = ExamensUiState.Error("An error occurred: ${e.message}")
            }
        }
    }
}

// ViewModel Factory to pass assureID
class ExamensViewModelFactory(private val assureID: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExamensViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExamensViewModel(assureID) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
