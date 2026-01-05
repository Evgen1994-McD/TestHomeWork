package com.example.core.data.repository

import com.example.core.data.database.TranslationDao
import com.example.core.data.mappers.toDomain
import com.example.core.data.mappers.toEntity
import com.example.core.domain.model.Translation
import com.example.core.domain.repository.HistoryRepository
import com.example.core.domain.repository.SortType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HistoryRepositoryImpl(
    private val translationDao: TranslationDao,
):HistoryRepository {
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


}