package com.example.testhomework.data.mapper

import com.example.testhomework.data.db.PhotoEntity
import com.example.testhomework.data.remote.dto.PhotoDto
import com.example.testhomework.domain.model.Photo
import com.example.testhomework.domain.model.PhotosPage
import com.example.testhomework.data.remote.dto.PhotosPageDto

fun PhotoDto.toDomain(): Photo {
    val thumb = url_sq ?: url_t ?: url_q ?: url_z ?: url_l ?: ""
    val full = url_l ?: url_z ?: url_q ?: url_t ?: url_sq ?:""
    return Photo(
        id = id,
        title = title,
        thumbnailUrl = thumb,
        fullUrl = full
    )
}

fun PhotosPageDto.toDomain(): PhotosPage {
    return PhotosPage(
        photos = photo.map { it.toDomain() },
        page = page,
        pages = pages,
        perPage = perpage,
        total = total.toIntOrNull() ?: 0
    )
}




fun PhotoEntity.toDomain(): Photo {
    return Photo(
        id = id,
        title = title,
        thumbnailUrl = thumbnailUrl,
        fullUrl = fullUrl
    )
}

fun Photo.toEntity(): PhotoEntity {
    return PhotoEntity(
        id = id,
        title = title,
        thumbnailUrl = thumbnailUrl,
        fullUrl = fullUrl
    )
}