package com.example.bininfo.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class BINRepository {
    private val api: BINApi = Network.getBINApi()

    fun getInfo(number: String): Flow<BINInfo> = flow {
        val info = api.getBINInfo(number)
        emit(info)
    }.flowOn(Dispatchers.IO)

}