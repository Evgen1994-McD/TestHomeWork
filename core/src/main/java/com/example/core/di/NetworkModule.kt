package com.example.core.di

import android.app.Application
import com.example.core.network.Api2
import com.example.core.network.ITunesApi
import com.example.core.network.NetworkClient
import com.example.core.network.RetrofitNetworkClient
import com.example.core.network.RetrofitNetworkClient2
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private  val TRACK_QUALIFIER = qualifier("track")
private  val SECOND_QUALIFIER = qualifier("second")

private fun createOkHttpClient(): OkHttpClient {
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    return OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
}

val networkModule = module {

    single<ITunesApi>(named(TRACK_QUALIFIER.value)) {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
    }

    single<Api2>(named(SECOND_QUALIFIER.value)) {
        Retrofit.Builder()
            .baseUrl("https://reqbin.com/echo/")
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api2::class.java)
    }

    single<Application> { androidContext() as Application }

    // Регистрация NetworkClient с qualifier'ами
    single<NetworkClient>(named(TRACK_QUALIFIER.value)) {
        RetrofitNetworkClient(get(named(TRACK_QUALIFIER.value)))
    }

    single<NetworkClient>(named(SECOND_QUALIFIER.value)) {
        RetrofitNetworkClient2(get(named(SECOND_QUALIFIER.value)))
    }
}
