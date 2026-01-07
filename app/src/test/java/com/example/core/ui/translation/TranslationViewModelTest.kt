package com.example.core.ui.translation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.core.domain.usecase.TranslateUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import okio.IOException
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class TranslationViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var viewModel: TranslationViewModel
    private val useCase: TranslateUseCase = mock()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = TranslationViewModel(useCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test - translate success flow`() = testScope.runTest {
        // Arrange
        viewModel.setSourceText("Hi")
        whenever(useCase("Hi", "en", "ru")).thenReturn(Result.success("Привет"))

        // Act
        viewModel.translate()
        testScheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertEquals("Привет", state.translatedText)
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)

    }

@Test
    fun `state is Loading when request`() = testScope.runTest {
        // Arrange
        viewModel.setSourceText("Hi")
        whenever(useCase("Hi", "en", "ru")).thenReturn(Result.success("Привет"))
        //act
        viewModel.translate()
        testScheduler.advanceUntilIdle()
        Thread.sleep(50)

        //Assert
        val state = viewModel.uiState.value
        Assertions.assertEquals(!state.isLoading, true)
    }


    @Test
    fun `correct state when sourceText if empty`() = testScope.runTest {
        viewModel.setSourceText("")

        viewModel.translate()
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        Assertions.assertEquals(state.errorMessage, "Введите текст для перевода")

    }

    @Test
    fun `correct state when language is identical`() = testScope.runTest {
        viewModel.setSourceText("Привет")
        viewModel.setSourceLanguage("ru")
        viewModel.setTargetLanguage("ru")

        viewModel.translate()
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        Assertions.assertEquals(state.errorMessage, "Выберите разные языки")
    }

    @Test
    fun `errorMessage clear when next request`() = testScope.runTest {
        viewModel.setSourceText("")
        whenever(useCase("Hi", "en", "ru")).thenReturn(Result.success("Привет"))


        viewModel.translate()
        testScheduler.advanceUntilIdle()
        viewModel.setSourceText("Hi")
        viewModel.translate()
        testScheduler.advanceUntilIdle()


        val state = viewModel.uiState.value
        Assertions.assertEquals(state.translatedText, "Привет" )
        Assertions.assertEquals(state.errorMessage, null)
    }

    @Test
    fun `disconnect from network test`() = testScope.runTest {
        viewModel.setSourceText("Привет")
        viewModel.setSourceLanguage("ru")
        viewModel.setTargetLanguage("en")
        whenever(useCase("Привет", "ru", "en")).thenReturn(Result.failure(exception = IOException("Нет интернета")))

        viewModel.translate()
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value

        Assertions.assertEquals(state.errorMessage, "Нет интернета")
        Assertions.assertFalse(state.isLoading)


    }


}