package com.example.core.data.dataSource.interfaces

interface TranslateDataSource {
    suspend fun translate(  sourceText: String,
                            sourceLanguage: String,
                            targetLanguage: String):Result<String>
}