package com.example.core.domain.usecase

import com.example.core.domain.repository.LocalRepository
import com.example.core.domain.repository.TranslationRepository

class TranslateUseCase(
    private val repository: TranslationRepository,
    private val localRepository: LocalRepository
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
        val cached = localRepository.translate(sourceText, sourceLanguage, targetLanguage)
        if (cached.isSuccess) return cached else {
            val remoteData = repository.translate(sourceText, sourceLanguage, targetLanguage)
            if (remoteData.isSuccess) {
                localRepository.saveTranslationToCache(
                    translatedText = remoteData.getOrNull()?:"",
                    sourceLanguage = sourceLanguage,
                    targetLanguage = targetLanguage,
                    sourceText = sourceText
                )
            }
            return remoteData
        }
    }
}

