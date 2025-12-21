package com.example.core.network

import retrofit2.http.GET
import retrofit2.http.Query

interface Api1 {
    @GET("/search?entity=song")
    suspend fun getSong(@Query("term") text: String)
            : Response
}