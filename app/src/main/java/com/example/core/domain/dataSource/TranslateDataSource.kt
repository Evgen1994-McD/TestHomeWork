package com.example.core.domain.dataSource

interface TranslateDataSource {
    suspend fun translate(  sourceText: String,
                            sourceLanguage: String,
                            targetLanguage: String):Result<String>
}