package com.example.core.network

import com.example.core.network.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}