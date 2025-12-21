package com.example.home.domain

import com.example.feature_home.domain.model.SomeBody
import com.example.feature_home.domain.model.Track

interface GetDataUseCase {
    suspend fun getTracks(expression: String): List<Track>
    suspend fun getSomeBody(expression: String): List<SomeBody>
}