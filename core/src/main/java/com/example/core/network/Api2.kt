package com.example.core.network

import com.example.core.network.dto.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api2 {
    @GET("/search?entity=song")
    suspend fun getSomebody(@Query("term") text: String)
            : Response
}