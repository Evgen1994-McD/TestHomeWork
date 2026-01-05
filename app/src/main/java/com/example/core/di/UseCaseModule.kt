package com.example.core.di

import com.example.core.domain.usecase.DeleteTranslationUseCase
import com.example.core.domain.usecase.GetTranslationHistoryUseCase
import com.example.core.domain.usecase.TranslateUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val useCaseModule = module {

    factory{
        DeleteTranslationUseCase(get())
    }

    factory { GetTranslationHistoryUseCase(get()) }

    factory { TranslateUseCase(get(named("remote")), get(named("local"))) }
}