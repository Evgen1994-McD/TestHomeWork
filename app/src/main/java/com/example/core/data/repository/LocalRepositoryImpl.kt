package com.example.core.data.repository

import com.example.core.domain.dataSource.LocalDataSource
import com.example.core.domain.repository.LocalRepository

class LocalRepositoryImpl(
    private val localDataSource: LocalDataSource
) :LocalRepository{
    override suspend fun translate(
        sourceText: String,
        sourceLanguage: String,
        targetLanguage: String
    ): Result<String> {
      return localDataSource.translate(sourceText,sourceLanguage,targetLanguage)

    }

    override suspend fun saveTranslationToCache(
        sourceText: String,
        translatedText: String,
        sourceLanguage: String,
        targetLanguage: String,
    ){
        localDataSource.saveTranslation(
            sourceText = sourceText,
            translatedText = translatedText,
            sourceLanguage = sourceLanguage,
            targetLanguage = targetLanguage
        )
    }
}