package com.example.core.ui.history

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.core.domain.repository.SortType
import com.example.core.domain.model.Translation
import com.example.core.domain.usecase.DeleteTranslationUseCase
import com.example.core.domain.usecase.GetTranslationHistoryUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.*
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class HistoryViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var viewModel: HistoryViewModel
    private val getHistoryUseCase = mock<GetTranslationHistoryUseCase>()
    private val deleteUseCase = mock<DeleteTranslationUseCase>()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loads translations on init`() = testScope.runTest {
        // Arrange
        val translations = listOf(
            Translation(1, "Hi", "Привет", "en", "ru", 1000)
        )
        whenever(getHistoryUseCase(SortType.DATE_DESC)).thenReturn(flowOf(translations))

        // Act
        viewModel = HistoryViewModel(getHistoryUseCase, deleteUseCase)
        testScheduler.advanceUntilIdle() // Ждем завершения корутины из init

        // Assert
        verify(getHistoryUseCase).invoke(SortType.DATE_DESC)
        Assert.assertEquals(1, viewModel.uiState.value.translations.size)
    }

    @Test
    fun `sets sort type and reloads`() = testScope.runTest {
        // Arrange
        whenever(getHistoryUseCase(any())).thenReturn(flowOf(emptyList()))
        viewModel = HistoryViewModel(getHistoryUseCase, deleteUseCase)
        testScheduler.advanceUntilIdle() // Ждем init

        // Act
        viewModel.setSortType(SortType.DATE_ASC)
        testScheduler.advanceUntilIdle() // Ждем перезагрузки

        // Assert
        verify(getHistoryUseCase).invoke(SortType.DATE_ASC)
        Assert.assertEquals(SortType.DATE_ASC, viewModel.uiState.value.sortType)
    }

    @Test
    fun `filters by source language`() = testScope.runTest {
        // Arrange
        val translations = listOf(
            Translation(1, "Hi", "Привет", "en", "ru", 1000),
            Translation(2, "Hola", "Привет", "es", "ru", 2000)
        )
        whenever(getHistoryUseCase(any())).thenReturn(flowOf(translations))
        viewModel = HistoryViewModel(getHistoryUseCase, deleteUseCase)
        testScheduler.advanceUntilIdle() // Ждем init

        // Act
        viewModel.setSourceLanguageFilter("en")

        // Assert
        val state = viewModel.uiState.value
        Assert.assertEquals(1, state.filteredTranslations.size)
        Assert.assertEquals("en", state.filteredTranslations[0].sourceLanguage)
    }

    @Test
    fun `deletes translation`() = testScope.runTest {
        // Arrange
        whenever(getHistoryUseCase(any())).thenReturn(flowOf(emptyList()))
        viewModel = HistoryViewModel(getHistoryUseCase, deleteUseCase)
        testScheduler.advanceUntilIdle()

        val translation = Translation(1, "Hi", "Привет", "en", "ru", 1000)

        // Act
        viewModel.deleteTranslation(translation)
        testScheduler.advanceUntilIdle()

        // Assert
        verify(deleteUseCase).invoke(translation)
    }
}