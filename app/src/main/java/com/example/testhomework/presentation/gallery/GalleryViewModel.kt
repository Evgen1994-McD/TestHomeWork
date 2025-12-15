package com.example.testhomework.presentation.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testhomework.data.repository.PhotosRepositoryImpl
import com.example.testhomework.domain.model.Photo
import kotlinx.coroutines.launch

class GalleryViewModel : ViewModel() {

    private val repository = PhotosRepositoryImpl()

    private val _photos = MutableLiveData<List<Photo>>(emptyList())
    val photos: LiveData<List<Photo>> = _photos

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private var currentPage = 1
    private var isLastPage = false
    private val pageSize = 50
    private var isLoadingPage = false

    fun loadFirstPage() {
        if (isLoadingPage) return
        currentPage = 1
        isLastPage = false
        _photos.value = emptyList()
        loadPage(currentPage, reset = true)
    }

    fun loadNextPage() {
        if (isLoadingPage || isLastPage) return
        loadPage(currentPage + 1, reset = false)
    }

    private fun loadPage(page: Int, reset: Boolean) {
        viewModelScope.launch {
            isLoadingPage = true
            _isLoading.value = true
            _errorMessage.value = null

            val result = repository.searchPhotos(page, pageSize)
            result
                .onSuccess { photosPage ->
                    currentPage = photosPage.page
                    isLastPage = photosPage.page >= photosPage.pages
                    val currentList = if (reset) emptyList() else _photos.value.orEmpty()
                    _photos.value = currentList + photosPage.photos
                }
                .onFailure { throwable ->
                    _errorMessage.value = throwable.message ?: "Unknown error"
                }

            _isLoading.value = false
            isLoadingPage = false
        }
    }
}


