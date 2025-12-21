package com.example.feature_home.di

import com.example.feature_home.domain.GetSomeBodyUseCase
import com.example.feature_home.domain.GetTrackUseCase
import com.example.feature_home.domain.impl.GetSomeBodyUseCaseImpl
import com.example.feature_home.domain.impl.GetTrackUseCaseImpl
import org.koin.dsl.module

val homeUseCaseModule = module {
    single<GetSomeBodyUseCase> { GetSomeBodyUseCaseImpl(get()) }
    single<GetTrackUseCase> { GetTrackUseCaseImpl(get()) }
}