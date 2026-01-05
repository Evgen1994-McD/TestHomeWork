package com.example.core.ui.translation

    import androidx.arch.core.executor.testing.InstantTaskExecutorRule
    import com.example.core.domain.usecase.TranslateUseCase
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.ExperimentalCoroutinesApi
    import kotlinx.coroutines.test.*
    import org.junit.*
    import org.junit.Assert.assertEquals
    import org.junit.Assert.assertFalse
    import org.junit.Assert.assertNull
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
        fun `quick test - translate success flow`() = testScope.runTest {
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
}