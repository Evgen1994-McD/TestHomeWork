package com.example.testhomework.data.remote.dto

data class FlickrSearchResponse(
    val photos: PhotosPageDto?,
    val stat: String,
    val code: Int? = null,
    val message: String? = null
)

data class PhotosPageDto(
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val total: String,
    val photo: List<PhotoDto>
)

data class PhotoDto(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int? = null,
    val title: String,
    val ispublic: Int,
    val isfriend: Int,
    val isfamily: Int,
    val url_sq: String? = null, // квадратная миниатюра 75x75
    val url_t: String? = null, // миниатюра 100px
    val url_q: String? = null, // квадратная миниатюра 150x150
    val url_z: String? = null, // среднее разрешение
    val url_l: String? = null  // крупное разрешение
)


