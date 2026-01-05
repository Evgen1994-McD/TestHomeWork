package com.example.core.data.dto

data class TranslationDTO(
    val id: Long = 0,
    val sourceText: String,
    val translatedText: String,
    val sourceLanguage: String,
    val targetLanguage: String,
    val timestamp: Long = System.currentTimeMillis()
)