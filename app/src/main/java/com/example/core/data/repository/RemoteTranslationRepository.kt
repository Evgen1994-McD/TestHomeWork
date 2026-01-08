package com.example.core.data.repository

import com.example.core.domain.dataSource.TranslateDataSource
import com.example.core.data.database.TranslationDao
import com.example.core.domain.repository.TranslationRepository as TranslationRepositoryDomain

class RemoteTranslationRepository(
    private val translationDao: TranslationDao,
    private val remoteDataSource: TranslateDataSource
) : TranslationRepositoryDomain {
    
    override suspend fun translate(
        sourceText: String,
        sourceLanguage: String,
        targetLanguage: String
    ): Result<String> {
        val cached = translationDao.getTranslation(sourceText, sourceLanguage, targetLanguage)
        if (cached != null) {
            return Result.success(cached.translatedText)
        }

     return remoteDataSource.translate(sourceText,sourceLanguage,targetLanguage)
    }



}

