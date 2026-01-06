package com.example.scainterclients.services

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// Data class for the login request body
data class LoginRequest(
    val username: String,
    val password: String
)

// Data class for the expected login response.
// Adjust the fields (e.g., token, userDetails) to match your actual API response.
data class LoginResponse(
    val familleID: Int
)

interface ApiInterface {
    /**
     * Performs a login request.
     * @param loginRequest The user's credentials.
     * @return A Retrofit Response containing the LoginResponse.
     */
    @POST("mobileapp/assurepostapi.php") // <-- IMPORTANT: Replace "api/login" with your actual login endpoint path.
    suspend fun fetch_assure_post(@Body loginRequest: LoginRequest): Response<LoginResponse>

}
