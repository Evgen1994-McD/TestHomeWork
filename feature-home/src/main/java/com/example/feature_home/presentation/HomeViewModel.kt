package com.example.feature_home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature_home.domain.GetSomeBodyUseCase
import com.example.feature_home.domain.GetTrackUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getDataUseCase: GetSomeBodyUseCase,
    private val getTrackUseCase: GetTrackUseCase
):ViewModel() {
    
    private val traclkFlow = MutableStateFlow<String>("")
    val trackState: StateFlow<String> = traclkFlow.asStateFlow()

    private val someBodyFlow = MutableStateFlow<String>("")
    val someBodyState: StateFlow<String> = someBodyFlow.asStateFlow()

    fun getTracks(expression:String) = viewModelScope.launch {
        try {
            val tracks = getTrackUseCase.getTracks(expression)
            traclkFlow.value = "Найдено треков: ${tracks.size}"
        } catch (e: Exception) {
            traclkFlow.value = "Ошибка при поиске треков"
        }
    }

    fun getSomeBody(expression: String) = viewModelScope.launch {
        try {
            val someBody = getDataUseCase.getSomeBody(expression)
            someBodyFlow.value = "Найдено результатов: ${someBody.size}"
        } catch (e: Exception) {
            someBodyFlow.value = "Ошибка при поиске результатов"
        }
    }
}