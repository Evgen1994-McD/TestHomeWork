package com.example.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature_home.domain.model.SomeBody
import com.example.feature_home.domain.model.Track
import com.example.home.domain.GetSomeBodyUseCase
import com.example.home.domain.GetTrackUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getDataUseCase: GetSomeBodyUseCase,
    private val getTrackUseCase: GetTrackUseCase
):ViewModel() {
    private val traclkFlow = MutableStateFlow<List<Track>>(emptyList())
    val trackState: StateFlow<List<Track>> = traclkFlow.asStateFlow()

    private val someBodyFlow = MutableStateFlow<List<SomeBody>>(emptyList())
    val someBodyState: StateFlow<List<SomeBody>> = someBodyFlow.asStateFlow()

    fun getTracks(expression:String) = viewModelScope.launch {
      traclkFlow.value=  getTrackUseCase.getTracks(expression)
    }

    fun getSomeBody(expression: String) = viewModelScope.launch {
        someBodyFlow.value = getDataUseCase.getSomeBody(expression)
    }
}