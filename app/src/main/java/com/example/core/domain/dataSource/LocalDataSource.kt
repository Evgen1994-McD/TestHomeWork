package com.example.core.domain.dataSource

interface LocalDataSource {
    suspend fun translate(  sourceText: String,
                            sourceLanguage: String,
                            targetLanguage: String):Result<String>
    suspend fun saveTranslation(
        id: Long = 0,
        sourceText: String,
        translatedText: String,
        sourceLanguage: String,
        targetLanguage: String,
        timestamp: Long = System.currentTimeMillis()
    )
}