package com.example.core.data.dataSource

import com.example.core.data.api.GoogleTranslateApi
import com.example.core.data.database.TranslationDao
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RemoteDataSourceTest {

    private val mockDao = mockk<TranslationDao>(relaxed = true)
    private val mockApi = mockk<GoogleTranslateApi>()
    private val dataSource = RemoteDataSource(mockDao, mockApi)

    @Test
    fun `success when api returns string`() = runTest {
        coEvery {
            mockApi.translate(
                sourceLanguage = "en",
                targetLanguage = "ru",
                query = "Hi"
            )
        } returns listOf("Привет")

        val result = dataSource.translate("Hi", "en", "ru")

        assertTrue(result.isSuccess)
        assertEquals("Привет", result.getOrNull())
    }

    @Test
    fun `success when api returns list of strings`() = runTest {
        coEvery {
            mockApi.translate(
                query = "Hello",
                sourceLanguage = "en",
                targetLanguage = "ru"
            )
        } returns listOf(listOf("Привет"))

        val result = dataSource.translate("Hello", "en", "ru")

        assertTrue(result.isSuccess)
        assertEquals("Привет", result.getOrNull())
    }

    @Test
    fun `failure when api throws exception`() = runTest {
        coEvery {
            mockApi.translate(
                sourceLanguage = "en",
                targetLanguage = "ru",
                query = "Hi"
            )
        } throws Exception("Network error")

        val result = dataSource.translate("Hi", "en", "ru")

        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `failure when empty response`() = runTest {
        coEvery {
            mockApi.translate(
                sourceLanguage = "en",
                targetLanguage = "ru",
                query = "Hi"
            )
        } returns emptyList()

        val result = dataSource.translate("Hi", "en", "ru")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("Empty") == true)
    }

    @Test
    fun `failure when invalid response type`() = runTest {
        coEvery {
            mockApi.translate(
                sourceLanguage = "en",
                targetLanguage = "ru",
                query = "Hi"
            )
        } returns listOf(123)

        val result = dataSource.translate("Hi", "en", "ru")

        assertTrue(result.isFailure)
    }

    @Test
    fun `saves to database on success`() = runTest {
        coEvery {
            mockApi.translate(
                sourceLanguage = "en",
                targetLanguage = "ru",
                query = "Hi"
            )
        } returns listOf("Привет")

        dataSource.translate("Hi", "en", "ru")

        coVerify { mockDao.insertTranslation(any()) }
    }

}

