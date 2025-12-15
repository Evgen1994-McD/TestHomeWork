package com.example.testhomework.data.mapper

import com.example.testhomework.data.remote.dto.PhotoDto
import com.example.testhomework.domain.model.Photo
import com.example.testhomework.domain.model.PhotosPage
import com.example.testhomework.data.remote.dto.PhotosPageDto

fun PhotoDto.toDomain(): Photo {
    val thumb = url_q ?: url_z ?: url_l ?: ""
    val full = url_l ?: url_z ?: url_q ?: ""
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


