package com.example.core.data.dataSource

import androidx.collection.LruCache
import com.example.core.domain.dataSource.LocalDataSource
import com.example.core.data.dto.TranslationDTO

class LocalDataSourceImpl : LocalDataSource {
    private val cache = object : LruCache<String, TranslationDTO>(10) {
        override fun sizeOf(key: String, value: TranslationDTO): Int = 1
    }

   override suspend fun saveTranslation(
        id: Long,
        sourceText: String,
        translatedText: String,
        sourceLanguage: String,
        targetLanguage: String,
        timestamp: Long
    ) {
        val key = generateKey(sourceText, sourceLanguage, targetLanguage)
        val model = TranslationDTO(
            id = id,
            sourceText = sourceText,
            translatedText = translatedText,
            sourceLanguage = sourceLanguage,
            targetLanguage = targetLanguage,
            timestamp = timestamp
        )
        cache.put(key, model)
    }


    override suspend fun translate(
        sourceText: String,
        sourceLanguage: String,
        targetLanguage: String
    ): Result<String> {
        val key = generateKey(sourceText, sourceLanguage, targetLanguage)
        val cached = cache.get(key)
        if (cached != null) {
            return Result.success(cached.translatedText)
        } else
            return Result.failure(Exception("Перевод не найден"))


    }


    private fun generateKey(
        originalText: String,
        sourceLanguage: String,
        targetLanguage: String
    ): String {
        return "${originalText.lowercase()}${sourceLanguage.lowercase()}${targetLanguage.lowercase()}"
    }
}