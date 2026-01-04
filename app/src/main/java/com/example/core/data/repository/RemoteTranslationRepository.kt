package com.example.core.data.repository

import com.example.core.data.dataSource.RemoteDataSource
import com.example.core.data.database.TranslationDao
import com.example.core.data.database.TranslationEntity
import com.example.core.domain.model.Translation
import com.example.core.domain.repository.SortType
import com.example.core.domain.repository.TranslationRepository as TranslationRepositoryDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RemoteTranslationRepository(
    private val translationDao: TranslationDao,
    private val remoteDataSource: RemoteDataSource
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

    override fun getAllTranslations(): Flow<List<Translation>> {
        return translationDao.getAllTranslations().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun deleteTranslation(translation: Translation) {
        translationDao.deleteTranslation(translation.toEntity())
    }

    override fun getTranslationsFiltered(
        sourceLang: String?,
        targetLang: String?
    ): Flow<List<Translation>> {
        return translationDao.getTranslationsFiltered(sourceLang, targetLang).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAllTranslationsSorted(sortType: SortType): Flow<List<Translation>> {
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

