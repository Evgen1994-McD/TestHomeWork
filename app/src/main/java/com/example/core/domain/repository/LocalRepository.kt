package com.example.core.domain.repository

interface LocalRepository {
    suspend fun translate(
        sourceText: String,
        sourceLanguage: String,
        targetLanguage: String
    ): Result<String>

    suspend fun saveTranslationToCache(
        sourceText: String,
        translatedText: String,
        sourceLanguage: String,
        targetLanguage: String,
    )
}