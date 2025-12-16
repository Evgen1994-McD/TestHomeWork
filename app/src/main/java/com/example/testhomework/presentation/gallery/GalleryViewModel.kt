package com.example.testhomework.presentation.gallery

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.testhomework.data.repository.PhotosRepositoryImpl
import com.example.testhomework.util.ErrorHandler
import kotlinx.coroutines.launch

class GalleryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PhotosRepositoryImpl()

    private val _uiState = MutableLiveData<GalleryUiState>(GalleryUiState.Loading())
    val uiState: LiveData<GalleryUiState> = _uiState

    private var currentPage = 1
    private var isLastPage = false
    private val pageSize = 50
    private var isLoadingPage = false

    fun loadFirstPage() {
        if (isLoadingPage) return
        currentPage = 1
        isLastPage = false
        _uiState.value = GalleryUiState.Loading()
        loadPage(currentPage, reset = true)
    }

    fun loadNextPage() {
        if (isLoadingPage || isLastPage) return
        val currentState = _uiState.value
        if (currentState is GalleryUiState.Success) {
            _uiState.value = currentState.copy(isLoadingMore = true)
        }
        loadPage(currentPage + 1, reset = false)
    }

    private fun loadPage(page: Int, reset: Boolean) {
        viewModelScope.launch {
            isLoadingPage = true

            val result = repository.searchPhotos(page, pageSize)
            result
                .onSuccess { photosPage ->
                    currentPage = photosPage.page
                    isLastPage = photosPage.page >= photosPage.pages
                    val currentList = if (reset) {
                        emptyList()
                    } else {
                        when (val state = _uiState.value) {
                            is GalleryUiState.Success -> state.photos
                            is GalleryUiState.Error -> state.photos
                            else -> emptyList()
                        }
                    }
                    val newPhotos = currentList + photosPage.photos
                    _uiState.value = GalleryUiState.Success(
                        photos = newPhotos,
                        isLoadingMore = false
                    )
                }
                .onFailure { throwable ->
                    val currentPhotos = when (val state = _uiState.value) {
                        is GalleryUiState.Success -> state.photos
                        is GalleryUiState.Error -> state.photos
                        else -> emptyList()
                    }
                    val errorMessage = ErrorHandler.getErrorMessage(throwable, getApplication())
                    _uiState.value = GalleryUiState.Error(
                        message = errorMessage,
                        photos = currentPhotos
                    )
                }

            isLoadingPage = false
        }
    }
}


