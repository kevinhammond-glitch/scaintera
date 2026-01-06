package com.example.scainterclients

import com.example.scainterclients.services.ApiInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory // Corrected import

// The 'object' keyword should start on its own line to declare a singleton object
object RetrofitInstance {
    // Retrofit requires the base URL to end with a '/'.
    private const val BASE_URL = "https://www.scaintera-segam.com/"

    private val retrofit by lazy {
        // Create a logging interceptor
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Set level to BODY to see everything
        }

        // Create an OkHttpClient and add the interceptor
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // Attach the custom OkHttpClient
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Renamed to 'api' for brevity, which is a common convention.
    val apiInterface: ApiInterface by lazy {
        retrofit.create(ApiInterface::class.java)
    }
    //val apiConsultation: ApiConsultation by lazy {
       // retrofit.create(ApiConsultation::class.java)
   // }
}
