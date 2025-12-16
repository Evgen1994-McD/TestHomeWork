package com.example.testhomework.presentation.gallery

import com.example.testhomework.domain.model.Photo

sealed class GalleryUiState {
    data class Success(
        val photos: List<Photo>,
        val isLoadingMore: Boolean = false
    ) : GalleryUiState()

    data class Loading(
        val isLoadingMore: Boolean = false
    ) : GalleryUiState()

    data class Error(
        val message: String,
        val photos: List<Photo> = emptyList()
    ) : GalleryUiState()
}
