package com.example.core.di

import com.example.core.data.repository.HistoryRepositoryImpl
import com.example.core.data.repository.LocalRepositoryImpl
import com.example.core.data.repository.RemoteTranslationRepository
import com.example.core.domain.repository.HistoryRepository
import com.example.core.domain.repository.LocalRepository
import com.example.core.domain.repository.TranslationRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    single<HistoryRepository> { HistoryRepositoryImpl(get()) }

    factory<TranslationRepository>(named("remote")){ RemoteTranslationRepository(get(), get(named("remoteSource"))) }

    single<LocalRepository>(named("local")) { LocalRepositoryImpl(get(named("localSource")))  }
}