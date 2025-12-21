package com.example.core.network

import com.example.core.network.dto.Response
import com.example.core.network.dto.somebody.SomebodySearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient2(private val api2: Api2) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (dto !is SomebodySearchRequest) {
            return Response().apply { resultCode = 400 }
        }
        return withContext(Dispatchers.IO) {
            try {
                val response = api2.getSomebody(dto.expression)
                response.apply { resultCode = 200 }
            } catch (e: Throwable) {
                Response().apply { resultCode = 500 }
            }
        }
    }
}
