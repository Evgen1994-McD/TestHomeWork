package com.task3

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class MyCustomInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        Log.d("interceptor", "Код ответа сервера: ${response.code}")
        return response
    }
}