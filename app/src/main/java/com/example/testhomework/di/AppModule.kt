package com.example.testhomework.di

import com.example.testhomework.data.repository.PhotosRepositoryImpl
import com.example.testhomework.domain.repository.PhotosRepository
import com.example.testhomework.domain.usecase.GetPhotosUseCase
import com.example.testhomework.presentation.details.PhotoDetailsViewModel
import com.example.testhomework.presentation.gallery.GalleryViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Модуль для настройки зависимостей приложения
 */
val appModule = module {
    // Data Layer
    single<PhotosRepository> { PhotosRepositoryImpl() }
    
    // Domain Layer - Use Cases
    factory { GetPhotosUseCase(get()) }
    
    // Presentation Layer - ViewModels
    viewModel { GalleryViewModel(androidApplication(), get()) }
    viewModel { PhotoDetailsViewModel(androidApplication()) }
}
