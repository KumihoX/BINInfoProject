package com.example.bininfo.network

@kotlinx.serialization.Serializable
data class CountryInfo(
    val numeric: String = "?",
    val alpha2: String = "?",
    val name: String = "?",
    val emoji: String = "?",
    val currency: String = "?",
    val latitude: Float ?= null,
    val longitude: Float ?= null
)
