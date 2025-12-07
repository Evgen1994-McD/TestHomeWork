package com.example.testhomework

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {
    @GET("get/json")
    fun getSomething(): Single<ApiResponse>

    @GET("translate_a/t?client=dict-chrome-ex")
    fun translate(
        @Query("sl") sourceLanguage: String,
        @Query("tl") targetLanguage: String,
        @Query("q") text: String
    ): Single<List<Any>>



    // Создайте новый файл или добавьте в ApiService.kt
    data class ApiResponse(
        val success: String
    )
}