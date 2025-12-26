package com.example.testhomework.presentation.details

sealed class PhotoDetailsUiState {
    data class Loading(val photoUrl: String, val photoTitle: String, val baseUrl: String) : PhotoDetailsUiState()
    data class Success(val photoUrl: String, val photoTitle: String, val baseUrl: String) : PhotoDetailsUiState()
    data class Error(val photoUrl: String, val photoTitle: String, val baseUrl: String, val message: String) : PhotoDetailsUiState()
}
