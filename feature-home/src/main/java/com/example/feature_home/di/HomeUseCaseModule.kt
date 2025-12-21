package com.example.feature_home.di

import com.example.home.domain.GetSomeBodyUseCase
import com.example.home.domain.GetTrackUseCase
import com.example.home.domain.impl.GetSomeBodyUseCaseImpl
import com.example.home.domain.impl.GetTrackUseCaseImpl
import org.koin.dsl.module

val homeUseCaseModule = module {
    single<GetSomeBodyUseCase> { GetSomeBodyUseCaseImpl(get()) }
    single<GetTrackUseCase> { GetTrackUseCaseImpl(get()) }
}