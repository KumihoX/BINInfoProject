package com.example.bininfo.network

import retrofit2.http.GET
import retrofit2.http.Path

interface BINApi {
    @GET("{number}")
    suspend fun getBINInfo(@Path("number") number: String): BINInfo
}