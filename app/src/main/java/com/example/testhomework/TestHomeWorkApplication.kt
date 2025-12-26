package com.example.testhomework

import android.app.Application
import com.example.testhomework.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TestHomeWorkApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidContext(this@TestHomeWorkApplication)
            modules(appModule)
        }
    }
}
