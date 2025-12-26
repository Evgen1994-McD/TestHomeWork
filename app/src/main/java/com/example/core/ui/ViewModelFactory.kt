package com.example.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core.domain.usecase.DeleteTranslationUseCase
import com.example.core.domain.usecase.GetTranslationHistoryUseCase
import com.example.core.domain.usecase.TranslateUseCase
import com.example.core.ui.history.HistoryViewModel
import com.example.core.ui.translation.TranslationViewModel

class ViewModelFactory(
    private val translateUseCase: TranslateUseCase,
    private val getTranslationHistoryUseCase: GetTranslationHistoryUseCase,
    private val deleteTranslationUseCase: DeleteTranslationUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(TranslationViewModel::class.java) -> {
                TranslationViewModel(translateUseCase) as T
            }
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(getTranslationHistoryUseCase, deleteTranslationUseCase) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

