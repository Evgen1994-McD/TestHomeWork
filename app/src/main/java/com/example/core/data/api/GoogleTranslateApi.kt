package com.example.core.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleTranslateApi {
    
    @GET("translate_a/t")
    suspend fun translate(
        @Query("client") client: String = "dict-chrome-ex",
        @Query("sl") sourceLanguage: String,
        @Query("tl") targetLanguage: String,
        @Query("q") query: String
    ): List<Any>
}

