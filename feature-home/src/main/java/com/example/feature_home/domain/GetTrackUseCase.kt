package com.example.home.domain

import com.example.feature_home.domain.model.SomeBody
import com.example.feature_home.domain.model.Track

interface GetTrackUseCase {

    suspend fun getTracks(expression: String): List<Track>
}