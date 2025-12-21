package com.example.core.network

import com.example.core.network.dto.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search?entity=song")
    suspend fun getSong(@Query("term") text: String)
            : Response
}