package com.example.core.domain.usecase

import com.example.core.domain.model.Translation
import com.example.core.domain.repository.SortType
import com.example.core.domain.repository.TranslationRepository
import kotlinx.coroutines.flow.Flow

class GetTranslationHistoryUseCase(
    private val repository: TranslationRepository
) {
    operator fun invoke(sortType: SortType = SortType.DATE_DESC): Flow<List<Translation>> {
        return repository.getAllTranslationsSorted(sortType)
    }
}

