package com.example.home.domain.impl

import com.example.feature_home.domain.model.SomeBody
import com.example.feature_home.domain.model.Track
import com.example.home.domain.GetDataUseCase
import com.example.home.domain.HomeRepository

class GetDataUseCaseImpl(
    private val homeRepository: HomeRepository
):GetDataUseCase {
    override suspend fun getTracks(expression:String):List<Track>{
        return homeRepository.searchTracks(expression)
    }

    override suspend fun getSomeBody(expression:String):List<SomeBody>{
        return homeRepository.searchSomeBody(expression)
    }
}