package com.example.testhomework.presentation.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.testhomework.util.ErrorHandler
import kotlinx.coroutines.launch

class PhotoDetailsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val _uiState = MutableLiveData<PhotoDetailsUiState>()
    val uiState: LiveData<PhotoDetailsUiState> = _uiState

    fun loadPhoto(photoUrl: String, baseUrl:String, photoTitle: String) {
        if (photoUrl.isEmpty()) {
           
            _uiState.value = PhotoDetailsUiState.Error(
                photoUrl = photoUrl,
                photoTitle = photoTitle,
                message = getApplication<Application>().getString(com.example.testhomework.R.string.error_generic)
            )
            return
        }

        _uiState.value = PhotoDetailsUiState.Loading(
            photoUrl = photoUrl,
            photoTitle = photoTitle
        )
    }

    fun onImageLoaded() {
        val currentState = _uiState.value
        if (currentState is PhotoDetailsUiState.Loading) {
            _uiState.value = PhotoDetailsUiState.Success(
                photoUrl = currentState.photoUrl,
                photoTitle = currentState.photoTitle
            )
        }
    }

    fun onImageLoadError(error: Throwable) {
        val currentState = _uiState.value
        if (currentState is PhotoDetailsUiState.Loading) {
            val errorMessage = ErrorHandler.getErrorMessage(error, getApplication())
            _uiState.value = PhotoDetailsUiState.Error(
                photoUrl = currentState.photoUrl,
                photoTitle = currentState.photoTitle,
                message = errorMessage
            )
        }
    }
}
