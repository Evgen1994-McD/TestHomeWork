package com.example.core.data.mappers
import org.junit.Assert.assertEquals // Правильный импорт!
import com.example.core.data.database.TranslationEntity
import com.example.core.domain.model.Translation
import org.junit.Test

class MappersTest{
    
    @Test
    fun `from Entity to Domain correct transfer`(){
        // Дано
        val entity = TranslationEntity(
            id = 1,
            sourceText = "Hello",
            translatedText = "Привет",
            sourceLanguage = "en",
            targetLanguage = "ru"
        )
        // Когда
        val domain = entity.toDomain()

        //Тогда должно быть
       assertEquals(1, domain.id)
       assertEquals("Hello", domain.sourceText)
    }

    @Test
    fun `from Domain to Entity correct transfer`(){
        // Дано
        val domain = Translation(
            id = 1,
            sourceText = "Hello",
            translatedText = "Привет",
            sourceLanguage = "en",
            targetLanguage = "ru"
        )
        // Когда
        val entity = domain.toEntity()

        //Тогда должно быть
        assertEquals(1, entity.id)
        assertEquals("Hello", entity.sourceText)
    }

}