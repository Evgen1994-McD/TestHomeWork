package com.example.core.data.repository

import com.example.core.data.database.TranslationDao
import com.example.core.data.database.TranslationEntity
import com.example.core.domain.repository.SortType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*
import org.mockito.kotlin.*

class HistoryRepositoryImplTest {

    private val dao: TranslationDao = mock()
    private val repo = HistoryRepositoryImpl(dao)

    @Test
    fun `getAllTranslationsSorted DATE_DESC should call correct dao method`() = runTest {
        // Given
        val entities = listOf(
            TranslationEntity(id = 1, sourceText = "Hello", translatedText = "Привет", sourceLanguage = "en", targetLanguage = "ru"),
            TranslationEntity(id = 2, sourceText = "World", translatedText = "Мир", sourceLanguage = "en", targetLanguage = "ru")
        )

        whenever(dao.getAllTranslations())
            .thenReturn(flowOf(entities))

        // When
        val result = repo.getAllTranslationsSorted(SortType.DATE_DESC).first()

        // Then
        assertEquals(2, result.size)
        assertEquals("Hello", result[0].sourceText)
        verify(dao).getAllTranslations() // Проверяем вызов правильного метода
    }

    @Test
    fun `getAllTranslationsSorted should map entities to domain`() = runTest {
        // Given
        val entity = TranslationEntity(
            id = 1,
            sourceText = "Test",
            translatedText = "Тест",
            sourceLanguage = "en",
            targetLanguage = "ru",
            timestamp = 123456789L
        )

        whenever(dao.getAllTranslationsSortedByTextAsc())
            .thenReturn(flowOf(listOf(entity)))

        // When
        val domainModels = repo.getAllTranslationsSorted(SortType.TEXT_ASC).first()

        // Then
        assertEquals(1, domainModels.size)
        assertEquals("Test", domainModels[0].sourceText)
        assertEquals("Тест", domainModels[0].translatedText)
        assertEquals("en", domainModels[0].sourceLanguage)
    }
}