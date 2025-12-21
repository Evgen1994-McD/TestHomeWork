package com.example.app

import android.app.Application
import com.example.core.di.networkModule
import com.example.core.di.repositoryModule
import com.example.core.di.useCaseModule
import com.example.core.di.viewModelModule
import com.example.feature_home.di.homeDataModule
import com.example.feature_home.di.homeUseCaseModule
import com.example.feature_home.di.homeViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyTestApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyTestApp)
            modules(networkModule,
                repositoryModule,
                useCaseModule,
                viewModelModule,
                homeDataModule,
                homeUseCaseModule,
                homeViewModelModule)

        }

    }
}