package com.example.core.data.mappers

import com.example.core.data.database.TranslationEntity
import com.example.core.domain.model.Translation

 fun TranslationEntity.toDomain(): Translation {
    return Translation(
        id = id,
        sourceText = sourceText,
        translatedText = translatedText,
        sourceLanguage = sourceLanguage,
        targetLanguage = targetLanguage,
        timestamp = timestamp
    )
}

 fun Translation.toEntity(): TranslationEntity {
    return TranslationEntity(
        id = id,
        sourceText = sourceText,
        translatedText = translatedText,
        sourceLanguage = sourceLanguage,
        targetLanguage = targetLanguage,
        timestamp = timestamp
    )
}