package com.example.core.ui.translation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.usecase.TranslateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TranslationUiState(
    val sourceText: String = "",
    val translatedText: String = "",
    val sourceLanguage: String = "en",
    val targetLanguage: String = "ru",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class TranslationViewModel(
    private val translateUseCase: TranslateUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(TranslationUiState())
    val uiState: StateFlow<TranslationUiState> = _uiState.asStateFlow()
    
    fun setSourceText(text: String) {
        _uiState.value = _uiState.value.copy(sourceText = text, errorMessage = null)
    }
    
    fun setSourceLanguage(language: String) {
        _uiState.value = _uiState.value.copy(sourceLanguage = language, errorMessage = null)
    }
    
    fun setTargetLanguage(language: String) {
        _uiState.value = _uiState.value.copy(targetLanguage = language, errorMessage = null)
    }
    
    fun translate() {
        val currentState = _uiState.value
        val sourceText = currentState.sourceText.trim()
        
        if (sourceText.isEmpty()) {
            _uiState.value = currentState.copy(errorMessage = "Введите текст для перевода")
            return
        }
        
        if (currentState.sourceLanguage == currentState.targetLanguage) {
            _uiState.value = currentState.copy(errorMessage = "Выберите разные языки")
            return
        }
        
        viewModelScope.launch {
            _uiState.value = currentState.copy(isLoading = true, errorMessage = null)
            
            val result = translateUseCase(
                sourceText = sourceText,
                sourceLanguage = currentState.sourceLanguage,
                targetLanguage = currentState.targetLanguage
            )
            
            result.fold(
                onSuccess = { translatedText ->
                    _uiState.value = currentState.copy(
                        translatedText = translatedText,
                        isLoading = false,
                        errorMessage = null
                    )
                },
                onFailure = { exception ->
                    _uiState.value = currentState.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Ошибка при переводе"
                    )
                }
            )
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

