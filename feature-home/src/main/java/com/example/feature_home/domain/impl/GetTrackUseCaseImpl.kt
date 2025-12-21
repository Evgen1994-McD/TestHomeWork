package com.example.feature_home.domain.impl

import com.example.feature_home.domain.model.SomeBody
import com.example.feature_home.domain.model.Track
import com.example.feature_home.domain.GetSomeBodyUseCase
import com.example.feature_home.domain.GetTrackUseCase
import com.example.feature_home.domain.HomeRepository

class GetTrackUseCaseImpl(
    private val homeRepository: HomeRepository
):GetTrackUseCase {


    override suspend fun getTracks(expression:String):List<Track>{
        return homeRepository.searchTracks(expression)
    }
}