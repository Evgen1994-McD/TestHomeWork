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
            android.util.Log.d("PhotosRepository", "Response stat: ${response.stat}, photos: ${response.photos}, code: ${response.code}, message: ${response.message}")
            if (response.stat == "ok") {
                if (response.photos == null) {
                    android.util.Log.e("PhotosRepository", "Flickr API returned null photos")
                    return Result.failure(IllegalStateException("Flickr API returned null photos"))
                }
                val photosPage = response.photos.toDomain()
                // Фильтруем фото без URL
                val validPhotos = photosPage.photos.filter { 
                    it.thumbnailUrl.isNotEmpty() && it.fullUrl.isNotEmpty() 
                }
                android.util.Log.d("PhotosRepository", "Valid photos count: ${validPhotos.size}")
                if (validPhotos.isEmpty()) {
                    android.util.Log.w("PhotosRepository", "No valid photos found after filtering")
                }
                Result.success(
                    photosPage.copy(photos = validPhotos)
                )
            } else {
                // Обрабатываем ошибку от API
                val errorMsg = response.message ?: "Flickr API error: ${response.stat}"
                android.util.Log.e("PhotosRepository", "Flickr API error: $errorMsg (code: ${response.code})")
                Result.failure(IllegalStateException(errorMsg))
            }
        } catch (e: Exception) {
            android.util.Log.e("PhotosRepository", "Exception in searchPhotos: ${e.message}", e)
            Result.failure(e)
        }
    }
}


