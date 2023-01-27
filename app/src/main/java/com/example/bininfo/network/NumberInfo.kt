package com.example.bininfo.network

@kotlinx.serialization.Serializable
data class NumberInfo(
    val length: Int? = null,
    val luhn: Boolean? = null
)
