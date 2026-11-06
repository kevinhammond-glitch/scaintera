package com.example.scainterclients.services

import kotlinx.serialization.SerialName
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.GET


data class LoginRequest(
    val username: String,
    val password: String
)
data class LoginResponse(
    val familleID: Int,
    val assureID: Int,
    val fullname : String,
    val statut : String,
    val reseaux : String,
    val affiliation : String,
    val souscripteur : String,
    val fraisexternes : String,
    val hospitalisation : String
)

data class Consultation(
    @SerializedName("ConsultationID") val consultationID: Int,
    @SerializedName("Fullname") val fullname: String,
    @SerializedName("Acte") val acte: String,
    @SerializedName("Prestaname") val prestaname: String,
    @SerializedName("dateCreated") val dateCreated: String
)

data class ApiResponseConsultation(
    @SerialName("success") val success: Boolean,
    @SerialName("data") val data: List<Consultation>
)

data class Examen(
    @SerializedName("DemandexamensID") val examenID: Int,
    @SerializedName("Fullname") val fullname: String,
    @SerializedName("Etat") val statut: String,
    @SerializedName("Prestaname") val prestaname: String,
    @SerializedName("dateCreated") val dateCreated: String
)

data class ApiResponseExamen(
    @SerialName("success") val success: Boolean,
    @SerialName("data") val data: List<Examen>
)

data class Hospit(
    @SerializedName("HospitalisationID") val hospitID: Int,
    @SerializedName("Fullname") val fullname: String,
    @SerializedName("Etat") val statut: String,
    @SerializedName("TypeHospit") val typehospit: String,
    @SerializedName("Prestaname") val prestaname: String,
    @SerializedName("dateCreated") val dateCreated: String
)

data class ApiResponseHospit(
    @SerialName("success") val success: Boolean,
    @SerialName("data") val data: List<Hospit>
)

data class ConsultationDetails(
    @SerializedName("Fraisexternes") val fraisexternes: String,
    @SerializedName("Sousname") val sousname: String,
    @SerializedName("InterneID") val interneID: String,
    @SerializedName("Fullname") val fullname: String,
    @SerializedName("Medicament") val medicament: String,
    @SerializedName("Posologie") val posologie: String,
    @SerializedName("Quantite") val quantite: String,
    @SerializedName("Medicament1") val medicament1: String,
    @SerializedName("Posologie1") val posologie1: String,
    @SerializedName("Quantite1") val quantite1: String,
    @SerializedName("Medicament2") val medicament2: String,
    @SerializedName("Posologie2") val posologie2: String,
    @SerializedName("Quantite2") val quantite2: String,
    @SerializedName("Medicament3") val medicament3: String,
    @SerializedName("Posologie3") val posologie3: String,
    @SerializedName("Quantite3") val quantite3: String,
    @SerializedName("Acte") val acte: String,
    @SerializedName("Doctor") val doctor: String,
    @SerializedName("Prestaname") val prestaname: String,
    @SerializedName("dateCreated") val dateCreated: String,
    @SerializedName("Rating") val userRating: String,
    @SerializedName("Comment") val userComment: String
)

data class ApiResponseConsultationDetails(
    @SerialName("success") val success: Boolean,
    @SerialName("data") val data: ConsultationDetails
)

data class ExamenDetails(
    @SerializedName("Fraisexternes") val fraisexternes: String,
    @SerializedName("Sousname") val sousname: String,
    @SerializedName("InterneID") val interneID: String,
    @SerializedName("Fullname") val fullname: String,
    @SerializedName("Examen") val examen: String,
    @SerializedName("Answer") val answer: String,
    @SerializedName("Examen1") val examen1: String,
    @SerializedName("Answer1") val answer1: String,
    @SerializedName("Examen2") val examen2: String,
    @SerializedName("Answer2") val answer2: String,
    @SerializedName("Examen3") val examen3: String,
    @SerializedName("Answer3") val answer3: String,
    @SerializedName("Examen4") val examen4: String,
    @SerializedName("Answer4") val answer4: String,
    @SerializedName("Examen5") val examen5: String,
    @SerializedName("Answer5") val answer5: String,
    @SerializedName("Examen6") val examen6: String,
    @SerializedName("Answer6") val answer6: String,
    @SerializedName("Examen7") val examen7: String,
    @SerializedName("Answer7") val answer7: String,
    @SerializedName("Etat") val etat: String,
    @SerializedName("Motif") val motif: String,
    @SerializedName("Medecin") val doctor: String,
    @SerializedName("Prestaname") val prestaname: String,
    @SerializedName("dateCreated") val dateCreated: String,
    @SerializedName("Rating") val userRating: String,
    @SerializedName("Comment") val userComment: String
)

data class ApiResponseExamenDetails(
    @SerialName("success") val success: Boolean,
    @SerialName("data") val data: ExamenDetails
)

data class HospitDetails(
    @SerializedName("Hospitalisation") val fraishospit: String,
    @SerializedName("Sousname") val sousname: String,
    @SerializedName("InterneID") val interneID: String,
    @SerializedName("Fullname") val fullname: String,
    @SerializedName("Etat") val etat: String,
    @SerializedName("Motif") val motif: String,
    @SerializedName("Jours") val duree: String,
    @SerializedName("TypeHospit") val type: String,
    @SerializedName("Prestaname") val prestaname: String,
    @SerializedName("dateCreated") val dateCreated: String,
    @SerializedName("Rating") val userRating: String,
    @SerializedName("Comment") val userComment: String
)

data class ApiResponseHospitDetails(
    @SerialName("success") val success: Boolean,
    @SerialName("data") val data: HospitDetails
)
data class ApiRembRequest(
    val assureID: Int,
    val type: String,
    val montant: String,
    val societe: String,
    val exclusions: String,
    val date: String,
    val etat: String,
    val prestaname: String,
    val prestataireID: String,
    val genrepresta: String
)
data class ApiRembResponse(
    val status: String,
    val message: String
)

data class RembDetails(
    @SerializedName("Etat") val etat: String,
    @SerializedName("Typeremboursement") val type: String,
    @SerializedName("Montant") val montant: String,
    @SerializedName("Rembourser") val rembourser: String,
    @SerializedName("Prestationdate") val prestadate: String,
    @SerializedName("dateCreated") val dateCreated: String
)

data class ApiResponseRembDetails(
    @SerialName("success") val success: Boolean,
    @SerialName("data") val data: List<RembDetails>
)

data class ApiRatingConsultRequest(
    val consultationID: Int,
    val rating: String,
    val comment: String,
)
data class ApiRatingConsultResponse(
    val status: String,
    val message: String
)

data class ApiRatingExamsRequest(
    val examenID: Int,
    val rating: String,
    val comment: String,
)
data class ApiRatingExamsResponse(
    val status: String,
    val message: String
)

data class ApiRatingHospitRequest(
    val hospitID: Int,
    val rating: String,
    val comment: String,
)
data class ApiRatingHospitResponse(
    val status: String,
    val message: String
)

interface ApiInterface {
    @POST("mobileapp/assurepostapi.php")
    suspend fun fetch_assure_post(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("mobileapp/consultgetapi.php")
    suspend fun getConsultations(@Query("assureID") assureID: Int): Response<ApiResponseConsultation>

    @GET("mobileapp/examgetapi.php")
    suspend fun getExams(@Query("assureID") assureID: Int): Response<ApiResponseExamen>

    @GET("mobileapp/hospitgetapi.php")
    suspend fun getHospits(@Query("assureID") assureID: Int): Response<ApiResponseHospit>

    @GET("mobileapp/consultdetailsgetapi.php")
    suspend fun getConsultationDetails(@Query("consultationID") consultationID: Int): Response<ApiResponseConsultationDetails>

    @GET("mobileapp/examendetailsgetapi.php")
    suspend fun getExamenDetails(@Query("examenID") examenID: Int): Response<ApiResponseExamenDetails>

    @GET("mobileapp/hospitdetailsgetapi.php")
    suspend fun getHospitDetails(@Query("hospitID") hospitID: Int): Response<ApiResponseHospitDetails>

    @POST("mobileapp/rembpostapi.php")
    suspend fun submitData(@Body data: ApiRembRequest): Response<ApiRembResponse>

    @GET("mobileapp/rembdetailsgetapi.php")
    suspend fun getRembDetails(@Query("assureID") assureID: Int): Response<ApiResponseRembDetails>

    @POST("mobileapp/ratingconsultpostapi.php")
    suspend fun postRating(@Body request: ApiRatingConsultRequest): Response<ApiRatingConsultResponse>

    @POST("mobileapp/ratingexamspostapi.php")
    suspend fun postRating(@Body request: ApiRatingExamsRequest): Response<ApiRatingExamsResponse>

    @POST("mobileapp/ratinghospitpostapi.php")
    suspend fun postRating(@Body request: ApiRatingHospitRequest): Response<ApiRatingHospitResponse>

}
