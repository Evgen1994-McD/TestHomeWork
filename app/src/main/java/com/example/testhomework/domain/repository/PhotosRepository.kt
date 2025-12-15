package com.example.testhomework.domain.repository

import com.example.testhomework.domain.model.PhotosPage

interface PhotosRepository {
    suspend fun searchPhotos(
        page: Int,
        pageSize: Int
    ): Result<PhotosPage>
}


