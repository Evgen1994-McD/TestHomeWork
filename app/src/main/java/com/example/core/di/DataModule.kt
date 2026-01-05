package com.example.core.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.core.data.api.GoogleTranslateApi
import com.example.core.data.api.GoogleTranslateService
import com.example.core.data.dataSource.RemoteDataSource
import com.example.core.data.dataSource.TranslateDataSource
import com.example.core.data.database.TranslationDao
import com.example.core.data.database.TranslationDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single<Application> { androidContext() as Application }

    single {
        Room.databaseBuilder(androidContext(), TranslationDatabase::class.java, "translation_database.db")
            .build()
    }

    single<TranslationDao> {
        val database :TranslationDatabase = get()
        database.translationDao()
    }

    single<GoogleTranslateApi>{
        GoogleTranslateService.api
    }

    single<TranslateDataSource> {
        RemoteDataSource(get(), get())
    }

}