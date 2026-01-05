package com.example.core.data.repository

import com.example.core.data.dataSource.RemoteDataSource
import com.example.core.data.dataSource.TranslateDataSource
import com.example.core.data.database.TranslationDao
import com.example.core.data.database.TranslationEntity
import com.example.core.domain.model.Translation
import com.example.core.domain.repository.SortType
import com.example.core.domain.repository.TranslationRepository as TranslationRepositoryDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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

