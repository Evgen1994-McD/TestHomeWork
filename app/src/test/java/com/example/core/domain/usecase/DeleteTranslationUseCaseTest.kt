package com.example.core.domain.usecase
import com.example.core.domain.model.Translation
import org.mockito.kotlin.*
import com.example.core.domain.repository.HistoryRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.assertEquals // Правильный импорт!

class DeleteTranslationUseCaseTest {
    private val historyRepository: HistoryRepository = mock()

    @Test
     fun `try to delete translation from base`() {
        //Данные
        val translation = Translation(
            sourceText = "hi",
            translatedText = "привет",
            sourceLanguage = "en",
            targetLanguage = "ru"
        )
        //Когда
       val unit = runBlocking {
            historyRepository.deleteTranslation(
                translation = translation
            )
        }

        //Тогда
        assertEquals(unit, Unit)
    }
}