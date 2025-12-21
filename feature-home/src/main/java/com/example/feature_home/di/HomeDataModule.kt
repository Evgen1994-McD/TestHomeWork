package com.example.feature_home.di

import com.example.home.data.HomeRepositoryImpl
import com.example.home.domain.HomeRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val homeDataModule = module {
    single<HomeRepository> {
        HomeRepositoryImpl(
            get(named("track")),
            get(named("second"))
        )
    }
}
