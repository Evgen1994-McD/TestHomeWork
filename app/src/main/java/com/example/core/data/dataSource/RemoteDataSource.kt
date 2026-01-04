package com.example.core.data.dataSource

import com.example.core.data.api.GoogleTranslateService
import com.example.core.data.database.TranslationDao
import com.example.core.data.database.TranslationEntity
import kotlinx.coroutines.delay
import kotlin.random.Random

class RemoteDataSource(
    private val translationDao: TranslationDao,
    private val api: com.example.core.data.api.GoogleTranslateApi = GoogleTranslateService.api
):TranslateDataSource {
    override suspend fun translate(
        sourceText: String,
        sourceLanguage: String,
        targetLanguage: String
    ): Result<String> {
        val delay =Random.nextLong(300, 1000)
        delay(delay)
        return try {
            val response = api.translate(
                sourceLanguage = sourceLanguage,
                targetLanguage = targetLanguage,
                query = sourceText
            )

            val translatedText = try {
                when {
                    response.isNotEmpty() && response[0] is String -> {
                        (response[0] as String)
                    }
                    response.isNotEmpty() && response[0] is List<*> -> {
                        val firstArray = response[0] as List<*>
                        when {
                            firstArray.isNotEmpty() && firstArray[0] is String -> {
                                firstArray[0] as String
                            }
                            firstArray.isNotEmpty() && firstArray[0] is List<*> -> {
                                val innerArray = firstArray[0] as List<*>
                                innerArray.filterIsInstance<String>().joinToString(" ")
                            }
                            firstArray.all { it is String } -> {
                                firstArray.filterIsInstance<String>().joinToString(" ")
                            }
                            else -> {
                                throw Exception("Translation text not found in response")
                            }
                        }
                    }
                    else -> throw Exception("Empty or invalid response")
                }
            } catch (e: Exception) {
                throw Exception("Failed to parse translation response: ${e.message}", e)
            }

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
}