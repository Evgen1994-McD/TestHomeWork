package com.example.testhomework.presentation.details

import com.example.testhomework.domain.model.Photo

sealed class PhotoDetailsUiState {
    data class Loading(val photoUrl: String, val photoTitle: String) : PhotoDetailsUiState()
    data class Success(val photoUrl: String, val photoTitle: String) : PhotoDetailsUiState()
    data class Error(val photoUrl: String, val photoTitle: String, val message: String) : PhotoDetailsUiState()
}
