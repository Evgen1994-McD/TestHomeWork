package com.example.core.data.repository
import org.junit.Assert.assertEquals // Правильный импорт!

import com.example.core.domain.dataSource.TranslateDataSource
import com.example.core.data.database.TranslationDao
import com.example.core.data.database.TranslationEntity
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.mockito.kotlin.*

class RemoteTranslationRepositoryTest {
    private val dao: TranslationDao = mock()
    private val dataSource: TranslateDataSource = mock()
    private val repo = RemoteTranslationRepository(dao, dataSource)
    @Test
    fun `check cache then remote`() = runTest {
        // Настраиваем
        val entity = TranslationEntity(
            sourceText = "Hi", translatedText = "Привет",
            sourceLanguage = "en",
            targetLanguage = "ru"
        )
        whenever(dao.getTranslation(any(), any(), any())).thenReturn(entity)
        whenever(dataSource.translate(any(), any(), any())).thenReturn(Result.success("Тест"))

        // Проверяем что берется из кеша
        val result = repo.translate("Hi", "en", "ru")
        assertEquals("Привет", result.getOrNull())

        // Проверяем что remote не вызывался
        verify(dataSource, never()).translate(any(), any(), any())
    }
}