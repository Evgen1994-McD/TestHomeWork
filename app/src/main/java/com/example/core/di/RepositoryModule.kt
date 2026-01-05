package com.example.core.di

import com.example.core.data.repository.HistoryRepositoryImpl
import com.example.core.data.repository.RemoteTranslationRepository
import com.example.core.domain.repository.HistoryRepository
import com.example.core.domain.repository.TranslationRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<HistoryRepository> { HistoryRepositoryImpl(get()) }

    factory<TranslationRepository>{ RemoteTranslationRepository(get(), get()) }
}