package com.example.core.domain.usecase

import com.example.core.domain.repository.TranslationRepository

class TranslateUseCase(
    private val repository: TranslationRepository
) {
    suspend operator fun invoke(
        sourceText: String,
        sourceLanguage: String,
        targetLanguage: String
    ): Result<String> {
        if (sourceText.isBlank()) {
            return Result.failure(IllegalArgumentException("Source text cannot be empty"))
        }
        
        if (sourceLanguage == targetLanguage) {
            return Result.failure(IllegalArgumentException("Source and target languages must be different"))
        }
        
        return repository.translate(sourceText, sourceLanguage, targetLanguage)
    }
}

