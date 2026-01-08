package com.example.core.data.repository

import com.example.core.data.dataSource.LocalDataSourceImpl
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.kotlin.*

class LocalRepositoryImplTest {

    private val dataSource: LocalDataSourceImpl = mock()
    private val repo = LocalRepositoryImpl(dataSource)

    @Test
    fun `translate should delegate to data source`() = runTest {
        // Given
        val expected = Result.success("Привет")
        whenever(dataSource.translate("Hello", "en", "ru"))
            .thenReturn(expected)

        // When
        val result = repo.translate("Hello", "en", "ru")

        // Then
        assertEquals(expected, result)
        verify(dataSource).translate("Hello", "en", "ru")
    }

    @Test
    fun `saveTranslationToCache should delegate to data source`() = runTest {
        // When
        repo.saveTranslationToCache(
            sourceText = "Hello",
            translatedText = "Привет",
            sourceLanguage = "en",
            targetLanguage = "ru"
        )

        // Then - исправленная проверка
        verify(dataSource).saveTranslation(
            id = anyLong(),
            sourceText = eq("Hello"),
            translatedText = eq("Привет"),
            sourceLanguage = eq("en"),
            targetLanguage = eq("ru"),
            timestamp = any() // ← ЛЮБОЙ timestamp!
        )
    }

    @Test
    fun `saveTranslationToCache should use default parameters`() = runTest {
        // When
        repo.saveTranslationToCache("A", "B", "en", "ru")

        // Then - проверяем что передаются default значения
        verify(dataSource).saveTranslation(
            id = eq(0L), // default из интерфейса
            sourceText = eq("A"),
            translatedText = eq("B"),
            sourceLanguage = eq("en"),
            targetLanguage = eq("ru"),
            timestamp = anyLong() // системное время
        )
    }
}