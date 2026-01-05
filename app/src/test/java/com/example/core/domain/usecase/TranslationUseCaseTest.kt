package com.example.core.domain.usecase

import com.example.core.domain.repository.LocalRepository
import com.example.core.domain.repository.TranslationRepository
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.*
import org.junit.Assert.assertEquals // Правильный импорт!

class TranslationUseCaseTest {
    private val repository: TranslationRepository = mock()
    private val localRepository: LocalRepository = mock()

@Test
fun `Fast Test repo`() = runTest {
    // Настройка
    whenever(localRepository.translate("Hi", "en", "ru"))
        .thenReturn(Result.success("Привет"))

    whenever(repository.translate("Hi", "en", "ru"))
        .thenReturn(Result.success("Привет!!!"))


    // Выполнение
    val result = localRepository.translate("Hi", "en", "ru")
    val remoteResult = repository.translate("Hi", "en", "ru")

    // Проверка
    assertEquals("Привет", result.getOrNull())
    assertEquals("Привет!!!", remoteResult.getOrNull())
}
}