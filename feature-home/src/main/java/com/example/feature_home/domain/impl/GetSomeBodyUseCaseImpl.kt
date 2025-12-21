package com.example.home.domain.impl

import com.example.feature_home.domain.model.SomeBody
import com.example.feature_home.domain.model.Track
import com.example.home.domain.GetSomeBodyUseCase
import com.example.home.domain.HomeRepository

class GetSomeBodyUseCaseImpl(
    private val homeRepository: HomeRepository
):GetSomeBodyUseCase {

    override suspend fun getSomeBody(expression:String):List<SomeBody>{
        return homeRepository.searchSomeBody(expression)
    }
}