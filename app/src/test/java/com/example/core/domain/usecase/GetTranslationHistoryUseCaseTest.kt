package com.example.core.domain.usecase

import com.example.core.domain.model.Translation
import com.example.core.domain.repository.HistoryRepository
import com.example.core.domain.repository.SortType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.*

class GetTranslationHistoryUseCaseTest {
    private val historyRepository: HistoryRepository = mock()

    @Test
    fun `getAllTranslationsSorted should return sorted translations`() = runTest {
        // Given (Дано)
        val sortType = SortType.DATE_DESC

        val testList = listOf(
            Translation(
                id = 1, // Добавьте ID если есть в модели
                sourceText = "hi",
                translatedText = "привет",
                sourceLanguage = "en",
                targetLanguage = "ru",
                timestamp = 1000L // Добавьте timestamp для сортировки
            ),
            Translation(
                id = 2,
                sourceText = "hello",
                translatedText = "здравствуйте",
                sourceLanguage = "en",
                targetLanguage = "ru",
                timestamp = 2000L
            ),
            Translation(
                id = 3,
                sourceText = "goodbye",
                translatedText = "до свидания",
                sourceLanguage = "en",
                targetLanguage = "ru",
                timestamp = 1500L
            )
        )

        // Сортируем как ожидаем (по DATE_DESC - новые первыми)
        val expectedSortedList = testList.sortedByDescending { it.timestamp }

        whenever(historyRepository.getAllTranslationsSorted(sortType))
            .thenReturn(flowOf(expectedSortedList))

        // When (Когда)
        val actualResult = historyRepository.getAllTranslationsSorted(sortType)
            .first() // Берем первое (и единственное) значение

        // Then (Тогда) - ПРОВЕРКИ!

        // 1. Проверяем количество элементов
        assertEquals("Должно быть 3 перевода", 3, actualResult.size)

        // 2. Проверяем сортировку (самый новый первым)
        assertEquals("Самый новый должен быть первым", 2000L, actualResult[0].timestamp)
        assertEquals("hello", actualResult[0].sourceText)

        assertEquals("Второй по времени", 1500L, actualResult[1].timestamp)
        assertEquals("goodbye", actualResult[1].sourceText)

        assertEquals("Самый старый последним", 1000L, actualResult[2].timestamp)
        assertEquals("hi", actualResult[2].sourceText)

        // 3. Проверяем что все элементы на месте
        assertTrue("Должен содержать 'hello'",
            actualResult.any { it.sourceText == "hello" })
        assertTrue("Должен содержать 'goodbye'",
            actualResult.any { it.sourceText == "goodbye" })
        assertTrue("Должен содержать 'hi'",
            actualResult.any { it.sourceText == "hi" })
    }
}