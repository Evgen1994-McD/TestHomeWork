package com.example.testhomework.domain.repository

import com.example.testhomework.domain.model.Photo
import com.example.testhomework.domain.model.PhotosPage

interface PhotosRepository {
    suspend fun searchPhotos(
        page: Int,
        pageSize: Int,
        searchQuery:String
    ): Result<PhotosPage>

    suspend fun savePhoto(photo: Photo): Long
    suspend fun getAllPhoto(query: String?): List<Photo>
    suspend fun getPhotoById(id: String): Photo
}


