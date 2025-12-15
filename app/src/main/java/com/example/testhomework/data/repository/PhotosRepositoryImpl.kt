package com.example.testhomework.data.repository

import com.example.testhomework.data.mapper.toDomain
import com.example.testhomework.data.remote.FlickrApiFactory
import com.example.testhomework.domain.model.PhotosPage
import com.example.testhomework.domain.repository.PhotosRepository

class PhotosRepositoryImpl : PhotosRepository {

    private val api = FlickrApiFactory.apiService

    override suspend fun searchPhotos(
        page: Int,
        pageSize: Int
    ): Result<PhotosPage> {
        return try {
            val response = api.searchPhotos(
                perPage = pageSize,
                page = page
            )
            if (response.stat == "ok") {
                Result.success(response.photos.toDomain())
            } else {
                Result.failure(IllegalStateException("Flickr error: ${response.stat}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


