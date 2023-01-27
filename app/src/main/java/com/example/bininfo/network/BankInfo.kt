package com.example.bininfo.network

@kotlinx.serialization.Serializable
data class BankInfo(
    val name: String = "?",
    val url: String = "?",
    val phone: String = "?",
    val city: String = "?"
)
