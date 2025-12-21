package com.example.feature_home.di

import com.example.feature_home.presentation.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val homeViewModelModule = module {
    viewModel<HomeViewModel> {
        HomeViewModel(
            get(),
            get()
        )
    }
}