package com.example.testhomework.data.repository

import android.content.Context
import coil.ImageLoader
import coil.request.ImageRequest
import com.example.testhomework.data.db.PhotoDb
import com.example.testhomework.data.mapper.toDomain
import com.example.testhomework.data.mapper.toEntity
import com.example.testhomework.data.remote.FlickrApiFactory
import com.example.testhomework.domain.model.Photo
import com.example.testhomework.domain.model.PhotosPage
import com.example.testhomework.domain.repository.PhotosRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class PhotosRepositoryImpl(
     private val photoDb: PhotoDb,
     private val imageLoader: ImageLoader,
    private val context:Context

) : PhotosRepository {

    private val api = FlickrApiFactory.apiService
    
    private suspend fun canLoadImage(url: String): Boolean {
        return try {
            val request = ImageRequest.Builder(context)
                .data(url)
                .size(coil.size.Size.ORIGINAL)
                .build()
            val result = imageLoader.execute(request)
            result.drawable != null
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun searchPhotos(
        page: Int,
        pageSize: Int,
        searchQuery: String
    ): Result<PhotosPage> {
        var allPhoto = photoDb.photoDao().getAllPhotos()
        return try {
            val response = if (searchQuery.isBlank()) {
                api.getRecentPhotos(
                    perPage = pageSize,
                    page = page
                )
            } else {
                api.searchPhotos(
                    perPage = pageSize,
                    page = page,
                    text = searchQuery
                )
            }

            if (response.stat == "ok") {
                if (response.photos == null) {
                    if (!allPhoto.isNullOrEmpty()) {
                        val photosFromDb = getAllPhoto(searchQuery)
                        return Result.success(
                            PhotosPage(
                                photos = photosFromDb,
                                page = page,
                                pages = 1,
                                perPage = pageSize,
                                total = photosFromDb.size
                            )
                        )
                    }
                    return Result.failure(IllegalStateException("Flickr API returned null photos"))
                }
                val photosPage = response.photos.toDomain()
                val validPhotos = photosPage.photos.filter {
                    it.thumbnailUrl.isNotEmpty() && it.fullUrl.isNotEmpty()
                }

                coroutineScope {
                    validPhotos.map { photo ->
                        async {
                            if (canLoadImage(photo.thumbnailUrl)) {
                                savePhoto(photo)
                                photo
                            } else {
                                null
                            }
                        }
                    }.mapNotNull { it.await() }
                }.let { successfullyLoadedPhotos ->
                    Result.success(
                        photosPage.copy(photos = successfullyLoadedPhotos)
                    )
                }
            } else {
                val errorMsg = response.message ?: "Flickr API error: ${response.stat}"
                if (!allPhoto.isNullOrEmpty()) {
                    val photosFromDb = getAllPhoto(searchQuery)
                    Result.success(
                        PhotosPage(
                            photos = photosFromDb,
                            page = page,
                            pages = 1,
                            perPage = pageSize,
                            total = photosFromDb.size
                        )
                    )
                } else {
                    Result.failure(IllegalStateException(errorMsg))
                }
            }
        } catch (e: Exception) {
            if (!allPhoto.isNullOrEmpty()) {
                val photosFromDb = getAllPhoto(searchQuery)
                Result.success(
                    PhotosPage(
                        photos = photosFromDb,
                        page = page,
                        pages = 1,
                        perPage = pageSize,
                        total = photosFromDb.size
                    )
                )
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun savePhoto(photo: Photo): Long {
        return photoDb.photoDao().savePhoto(photo.toEntity())
    }

    override suspend fun getAllPhoto(query: String?): List<Photo> {
        if (!query.isNullOrEmpty()) {
            return photoDb.photoDao().getAllPhotosByTitle(query).map { it.toDomain() }
        } else return photoDb.photoDao().getAllPhotos().map { it.toDomain() }
    }

    override suspend fun getPhotoById(id: String): Photo {
        return photoDb.photoDao().getPhotoById(id).toDomain()
    }
}
