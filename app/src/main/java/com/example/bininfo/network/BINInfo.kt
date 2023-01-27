package com.example.bininfo.network

import kotlinx.serialization.Serializable

@Serializable
data class BINInfo(
    val number: NumberInfo = NumberInfo(),
    val scheme: String = "?",
    val type: String = "?",
    val brand: String = "?",
    val prepaid: Boolean ?= null,
    val country: CountryInfo = CountryInfo(),
    val bank: BankInfo = BankInfo()
)
