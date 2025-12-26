package com.example.core.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.repository.SortType
import com.example.core.data.repository.TranslationRepository
import com.example.core.domain.model.Translation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HistoryUiState(
    val translations: List<Translation> = emptyList(),
    val filteredTranslations: List<Translation> = emptyList(),
    val selectedSourceLanguage: String? = null,
    val selectedTargetLanguage: String? = null,
    val sortType: SortType = SortType.DATE_DESC,
    val isLoading: Boolean = false
)

class HistoryViewModel(
    private val repository: TranslationRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()
    
    init {
        loadTranslations()
    }
    
    private var currentJob: kotlinx.coroutines.Job? = null
    
    private fun loadTranslations() {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            repository.getAllTranslationsSorted(_uiState.value.sortType).collect { translations ->
                val currentState = _uiState.value
                val filtered = translations.filter { translation ->
                    (currentState.selectedSourceLanguage == null || 
                     translation.sourceLanguage == currentState.selectedSourceLanguage) &&
                    (currentState.selectedTargetLanguage == null || 
                     translation.targetLanguage == currentState.selectedTargetLanguage)
                }
                _uiState.value = currentState.copy(
                    translations = translations,
                    filteredTranslations = filtered,
                    isLoading = false
                )
            }
        }
    }
    
    fun setSortType(sortType: SortType) {
        _uiState.value = _uiState.value.copy(sortType = sortType)
        loadTranslations()
    }
    
    fun setSourceLanguageFilter(language: String?) {
        val currentState = _uiState.value
        val filtered = currentState.translations.filter { translation ->
            (language == null || translation.sourceLanguage == language) &&
            (currentState.selectedTargetLanguage == null || 
             translation.targetLanguage == currentState.selectedTargetLanguage)
        }
        _uiState.value = currentState.copy(
            selectedSourceLanguage = language,
            filteredTranslations = filtered
        )
    }
    
    fun setTargetLanguageFilter(language: String?) {
        val currentState = _uiState.value
        val filtered = currentState.translations.filter { translation ->
            (currentState.selectedSourceLanguage == null || 
             translation.sourceLanguage == currentState.selectedSourceLanguage) &&
            (language == null || translation.targetLanguage == language)
        }
        _uiState.value = currentState.copy(
            selectedTargetLanguage = language,
            filteredTranslations = filtered
        )
    }
    
    fun deleteTranslation(translation: Translation) {
        viewModelScope.launch {
            repository.deleteTranslation(translation)
            // Обновление произойдет автоматически через Flow
        }
    }
    
    fun clearFilters() {
        val currentState = _uiState.value
        _uiState.value = currentState.copy(
            selectedSourceLanguage = null,
            selectedTargetLanguage = null,
            filteredTranslations = currentState.translations
        )
    }
}

