package com.example.core.data.dataSource

class LocalDataSource:TranslateDataSource {
    override suspend fun translate(
        sourceText: String,
        sourceLanguage: String,
        targetLanguage: String
    ): Result<String> {
        TODO("Not yet implemented")
    }
}