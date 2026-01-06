package com.example.scainterclients.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Data class to hold the user information
data class UserData(
    val familleID: String = "",
    val fullname: String = "",
    val statut: String = "",
    val reseaux: String = "",
    val souscripteur: String = "",
    val fraisexternes: String = "",
    val hospitalisation: String = ""
)

// Sealed interface for the user state
sealed interface UserUiState {
    object NotLoggedIn : UserUiState
    data class LoggedIn(val userData: UserData) : UserUiState
}

class UserViewModel : ViewModel() {

    private val _userState = MutableStateFlow<UserUiState>(UserUiState.NotLoggedIn)
    val userState: StateFlow<UserUiState> = _userState.asStateFlow()

    fun setUserData(userData: UserData) {
        _userState.value = UserUiState.LoggedIn(userData)
    }

    fun clearUserData() {
        _userState.value = UserUiState.NotLoggedIn
    }
}
