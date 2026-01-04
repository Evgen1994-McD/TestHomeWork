package com.example.core.di

import android.content.Context
import com.example.core.data.api.GoogleTranslateApi
import com.example.core.data.api.GoogleTranslateService
import com.example.core.data.dataSource.RemoteDataSource
import com.example.core.data.database.TranslationDatabase
import com.example.core.data.repository.TranslationRepository
import com.example.core.domain.repository.TranslationRepository as TranslationRepositoryDomain
import com.example.core.domain.usecase.DeleteTranslationUseCase
import com.example.core.domain.usecase.GetTranslationHistoryUseCase
import com.example.core.domain.usecase.TranslateUseCase

/**
 * Service locator при необходимости можно перевести на DI
 */
object AppModule {
    private var database: TranslationDatabase? = null
    private var repository: TranslationRepositoryDomain? = null
    private var translateUseCase: TranslateUseCase? = null
    private var historyUseCase: GetTranslationHistoryUseCase? = null
    private var deleteTranslationUseCase: DeleteTranslationUseCase? = null

    private var remoteDataSource: RemoteDataSource? = null

    fun getRemoteDataSource(context: Context):RemoteDataSource{
        if (remoteDataSource==null){
            val db = getDatabase(context)
           remoteDataSource = RemoteDataSource(db.translationDao(), GoogleTranslateService.api)
        }
        return remoteDataSource!!
    }


    fun getDatabase(context: Context): TranslationDatabase {
        if (database == null) {
            database = TranslationDatabase.getDatabase(context)
        }
        return database!!
    }
    
    fun getRepository(context: Context): TranslationRepositoryDomain {
        if (repository == null) {
            val db = getDatabase(context)
            repository = TranslationRepository(db.translationDao(), getRemoteDataSource(context))
        }
        return repository!!
    }
    
    fun getTranslateUseCase(context: Context): TranslateUseCase {
        if (translateUseCase == null) {
            translateUseCase = TranslateUseCase(getRepository(context))
        }
        return translateUseCase!!
    }
    
    fun getTranslationHistoryUseCase(context: Context): GetTranslationHistoryUseCase {
        if (historyUseCase == null) {
            historyUseCase = GetTranslationHistoryUseCase(getRepository(context))
        }
        return historyUseCase!!
    }
    
    fun getDeleteTranslationUseCase(context: Context): DeleteTranslationUseCase {
        if (deleteTranslationUseCase == null) {
            deleteTranslationUseCase = DeleteTranslationUseCase(getRepository(context))
        }
        return deleteTranslationUseCase!!
    }



}

