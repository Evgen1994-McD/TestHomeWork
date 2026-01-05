package com.example.core.di

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.ui.history.HistoryViewModel
import com.example.core.ui.translation.TranslationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel{
        HistoryViewModel(get(), get())
    }

    viewModel{
        TranslationViewModel(get())
    }
}