package com.example.testhomework.data.remote

import com.example.testhomework.data.remote.dto.FlickrSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApiService {

    @GET("services/rest")
    suspend fun searchPhotos(
        @Query("method") method: String = "flickr.photos.search",
        @Query("api_key") apiKey: String = FlickrConfig.API_KEY,
        @Query("format") format: String = "json",
        @Query("nojsoncallback") noJsonCallback: Int = 1,
        @Query("extras") extras: String = "url_q,url_z,url_l",
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
        @Query("text") text: String = "kittens"
    ): FlickrSearchResponse
}


