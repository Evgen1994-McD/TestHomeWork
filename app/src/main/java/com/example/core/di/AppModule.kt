package com.example.core.di

import android.content.Context
import androidx.room.Room
import com.example.core.data.database.TranslationDatabase
import com.example.core.data.repository.TranslationRepository

object AppModule {
    private var database: TranslationDatabase? = null
    private var repository: TranslationRepository? = null
    
    fun getDatabase(context: Context): TranslationDatabase {
        if (database == null) {
            database = TranslationDatabase.getDatabase(context)
        }
        return database!!
    }
    
    fun getRepository(context: Context): TranslationRepository {
        if (repository == null) {
            val db = getDatabase(context)
            repository = TranslationRepository(db.translationDao())
        }
        return repository!!
    }
}

