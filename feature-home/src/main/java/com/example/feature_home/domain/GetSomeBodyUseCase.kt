package com.example.feature_home.domain

import com.example.feature_home.domain.model.SomeBody
import com.example.feature_home.domain.model.Track

interface GetSomeBodyUseCase {

    suspend fun getSomeBody(expression: String): List<SomeBody>
}