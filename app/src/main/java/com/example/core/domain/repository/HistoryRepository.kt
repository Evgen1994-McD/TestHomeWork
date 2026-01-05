package com.example.core.domain.repository

import com.example.core.domain.model.Translation
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun getAllTranslations(): Flow<List<Translation>>

    suspend fun deleteTranslation(translation: Translation)

    fun getTranslationsFiltered(
        sourceLang: String?,
        targetLang: String?
    ): Flow<List<Translation>>

    fun getAllTranslationsSorted(sortType: SortType): Flow<List<Translation>>
}

enum class SortType {
    DATE_DESC,
    DATE_ASC,
    TEXT_ASC,
    TEXT_DESC
}
