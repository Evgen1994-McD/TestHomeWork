package com.example.core

import android.app.Application
import com.example.core.di.dataModule
import com.example.core.di.repositoryModule
import com.example.core.di.useCaseModule
import com.example.core.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TranslateApp:Application() {
    override fun onCreate() {
        super.onCreate()

    startKoin {
        androidContext(this@TranslateApp)
        modules(dataModule, repositoryModule, useCaseModule, viewModelModule)
    }

    }
}