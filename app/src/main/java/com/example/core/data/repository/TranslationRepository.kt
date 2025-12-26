package com.example.core.data.repository

import com.example.core.data.api.GoogleTranslateService
import com.example.core.data.database.TranslationDao
import com.example.core.data.database.TranslationEntity
import com.example.core.domain.model.Translation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TranslationRepository(
    private val translationDao: TranslationDao,
    private val api: com.example.core.data.api.GoogleTranslateApi = GoogleTranslateService.api
) {
    
    suspend fun translate(
        sourceText: String,
        sourceLanguage: String,
        targetLanguage: String
    ): Result<String> {
        // Проверяем кэш перед запросом к API
        val cached = translationDao.getTranslation(sourceText, sourceLanguage, targetLanguage)
        if (cached != null) {
            return Result.success(cached.translatedText)
        }
        
        // Если нет в кэше, делаем запрос к API
        return try {
            val response = api.translate(
                sourceLanguage = sourceLanguage,
                targetLanguage = targetLanguage,
                query = sourceText
            )
            
            // Парсим ответ Google Translate API
            // Ответ приходит в формате: [[["translated text", null, null, null], ...], ...]
            val translatedText = try {
                when {
                    response.isNotEmpty() && response[0] is List<*> -> {
                        val firstArray = response[0] as List<*>
                        if (firstArray.isNotEmpty() && firstArray[0] is List<*>) {
                            val innerArray = firstArray[0] as List<*>
                            if (innerArray.isNotEmpty() && innerArray[0] is String) {
                                innerArray[0] as String
                            } else {
                                throw Exception("Translation text not found in response")
                            }
                        } else {
                            throw Exception("Unexpected response structure")
                        }
                    }
                    else -> throw Exception("Empty or invalid response")
                }
            } catch (e: Exception) {
                throw Exception("Failed to parse translation response: ${e.message}", e)
            }
            
            // Сохраняем в базу данных
            val entity = TranslationEntity(
                sourceText = sourceText,
                translatedText = translatedText,
                sourceLanguage = sourceLanguage,
                targetLanguage = targetLanguage,
                timestamp = System.currentTimeMillis()
            )
            translationDao.insertTranslation(entity)
            
            Result.success(translatedText)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun getAllTranslations(): Flow<List<Translation>> {
        return translationDao.getAllTranslations().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    suspend fun deleteTranslation(translation: Translation) {
        translationDao.deleteTranslation(translation.toEntity())
    }
    
    fun getTranslationsFiltered(
        sourceLang: String?,
        targetLang: String?
    ): Flow<List<Translation>> {
        return translationDao.getTranslationsFiltered(sourceLang, targetLang).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    fun getAllTranslationsSorted(sortType: SortType): Flow<List<Translation>> {
        val flow = when (sortType) {
            SortType.DATE_DESC -> translationDao.getAllTranslations()
            SortType.DATE_ASC -> translationDao.getAllTranslationsSortedByDateAsc()
            SortType.TEXT_ASC -> translationDao.getAllTranslationsSortedByTextAsc()
            SortType.TEXT_DESC -> translationDao.getAllTranslationsSortedByTextDesc()
        }
        return flow.map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    private fun TranslationEntity.toDomain(): Translation {
        return Translation(
            id = id,
            sourceText = sourceText,
            translatedText = translatedText,
            sourceLanguage = sourceLanguage,
            targetLanguage = targetLanguage,
            timestamp = timestamp
        )
    }
    
    private fun Translation.toEntity(): TranslationEntity {
        return TranslationEntity(
            id = id,
            sourceText = sourceText,
            translatedText = translatedText,
            sourceLanguage = sourceLanguage,
            targetLanguage = targetLanguage,
            timestamp = timestamp
        )
    }
}

enum class SortType {
    DATE_DESC,
    DATE_ASC,
    TEXT_ASC,
    TEXT_DESC
}

