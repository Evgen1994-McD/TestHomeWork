package com.example.core.domain.usecase

import com.example.core.domain.model.Translation
import com.example.core.domain.repository.TranslationRepository

class DeleteTranslationUseCase(
    private val repository: TranslationRepository
) {
    suspend operator fun invoke(translation: Translation) {
        repository.deleteTranslation(translation)
    }
}

