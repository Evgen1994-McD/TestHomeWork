package com.example.testhomework.domain.model

data class PhotosPage(
    val photos: List<Photo>,
    val page: Int,
    val pages: Int,
    val perPage: Int,
    val total: Int
)


