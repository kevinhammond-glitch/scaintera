package com.example.scainterclients.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.scainterclients.RetrofitInstance
import com.example.scainterclients.services.ApiRatingConsultRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RatingConsultViewModel(private val consultationID: Int) : ViewModel() {

    private val _submissionStatus = MutableStateFlow<String?>(null)
    val submissionStatus: StateFlow<String?> = _submissionStatus.asStateFlow()

    fun postRating(rating: String, comment: String) {
        viewModelScope.launch {
            try {
                val request = ApiRatingConsultRequest(
                    consultationID = consultationID,
                    rating = rating,
                    comment = comment
                )

                // Note: Make sure your PHP expects these exact keys
                val response = RetrofitInstance.apiInterface.postRating(request)

                if (response.isSuccessful) {
                    _submissionStatus.value = "Success"
                } else {
                    _submissionStatus.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _submissionStatus.value = "Network Error: ${e.message}"
            }
        }
    }

    fun resetStatus() {
        _submissionStatus.value = null
    }
}

// THE FACTORY: This tells Android how to build your ViewModel with an ID
class RatingConsultViewModelFactory(private val consultationID: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RatingConsultViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RatingConsultViewModel(consultationID) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}