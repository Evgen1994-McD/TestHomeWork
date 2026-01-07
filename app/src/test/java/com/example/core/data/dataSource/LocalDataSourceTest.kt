package com.example.core.data.dataSource

import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class LocalDataSourceImplTest {

    private val dataSource = LocalDataSourceImpl()

    @Test
    fun `translate returns cached translation`() = runTest {
        dataSource.saveTranslation(1, "Hi", "Привет", "en", "ru", 123)

        val result = dataSource.translate("Hi", "en", "ru")

        assertTrue(result.isSuccess)
        assertEquals("Привет", result.getOrNull())
    }

    @Test
    fun `translate fails when not cached`() = runTest {
        val result = dataSource.translate("Unknown", "en", "ru")

        assertTrue(result.isFailure)
    }

    @Test
    fun `cache is case insensitive`() = runTest {
        dataSource.saveTranslation(1, "hello", "привет", "en", "ru", 123)

        val result = dataSource.translate("HELLO", "EN", "RU")

        assertTrue(result.isSuccess)
    }

    @Test
    fun `lru cache evicts oldest`() = runTest {
        for (i in 1..10) {
            dataSource.saveTranslation(i.toLong(), "text$i", "trans$i", "en", "ru", i.toLong())
        }

        dataSource.saveTranslation(11, "text11", "trans11", "en", "ru", 11)

        val firstResult = dataSource.translate("text1", "en", "ru")
        val lastResult = dataSource.translate("text11", "en", "ru")

        assertTrue(firstResult.isFailure)
        assertTrue(lastResult.isSuccess)
    }

    @Test
    fun `different languages create different cache entries`() = runTest {
        dataSource.saveTranslation(1, "Hello", "Привет", "en", "ru", 1)
        dataSource.saveTranslation(2, "Hello", "Hola", "en", "es", 2)

        val ruResult = dataSource.translate("Hello", "en", "ru")
        val esResult = dataSource.translate("Hello", "en", "es")

        assertEquals("Привет", ruResult.getOrNull())
        assertEquals("Hola", esResult.getOrNull())
    }

    @Test
    fun `update overwrites existing cache`() = runTest {
        dataSource.saveTranslation(1, "Hi", "Old", "en", "ru", 1)
        dataSource.saveTranslation(2, "Hi", "New", "en", "ru", 2)

        val result = dataSource.translate("Hi", "en", "ru")

        assertEquals("New", result.getOrNull())
    }
}