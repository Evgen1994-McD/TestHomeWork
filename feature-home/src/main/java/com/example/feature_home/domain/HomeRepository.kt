package com.example.home.domain

import com.example.feature_home.domain.model.SomeBody
import com.example.feature_home.domain.model.Track

interface HomeRepository {
    suspend fun searchTracks(expression: String): List<Track>
    suspend fun searchSomeBody(expression: String): List<SomeBody>
}