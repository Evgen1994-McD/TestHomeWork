package com.example.testhomework.util

import java.net.UnknownHostException
import java.net.SocketTimeoutException
import java.io.IOException
import javax.net.ssl.SSLException

object ErrorHandler {
    
    fun getErrorMessage(throwable: Throwable, context: android.content.Context): String {
        return when (throwable) {
            is UnknownHostException -> {
                context.getString(com.example.testhomework.R.string.error_no_internet)
            }
            is SocketTimeoutException -> {
                context.getString(com.example.testhomework.R.string.error_network)
            }
            is IOException -> {
                context.getString(com.example.testhomework.R.string.error_network)
            }
            is SSLException -> {
                context.getString(com.example.testhomework.R.string.error_network)
            }
            else -> {
                val message = throwable.message?.lowercase() ?: ""
                when {
                    message.contains("unable to resolve host") || 
                    message.contains("no address associated") ||
                    message.contains("network is unreachable") -> {
                        context.getString(com.example.testhomework.R.string.error_no_internet)
                    }
                    message.contains("timeout") || 
                    message.contains("connection") -> {
                        context.getString(com.example.testhomework.R.string.error_network)
                    }
                    message.contains("server") || 
                    message.contains("500") || 
                    message.contains("502") || 
                    message.contains("503") -> {
                        context.getString(com.example.testhomework.R.string.error_server)
                    }
                    else -> {
                        context.getString(com.example.testhomework.R.string.error_generic)
                    }
                }
            }
        }
    }
}

