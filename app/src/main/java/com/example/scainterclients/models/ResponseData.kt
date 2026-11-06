package com.example.scainterclients.models

data class ResponseData(
    val id: Int,
    val username: String,
    val password: Int
)

data class ReimbursementData(
    val type: String,
    val amount: String,
    val date: String
)