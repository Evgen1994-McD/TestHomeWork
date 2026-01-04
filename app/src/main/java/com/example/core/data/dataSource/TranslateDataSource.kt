package com.example.core.data.dataSource

interface TranslateDataSource {
    suspend fun translate(  sourceText: String,
                            sourceLanguage: String,
                            targetLanguage: String):Result<String>
}