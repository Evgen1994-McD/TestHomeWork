package com.example.core.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TranslationDao {
    
    @Query("SELECT * FROM translations WHERE sourceText = :sourceText AND sourceLanguage = :sourceLang AND targetLanguage = :targetLang LIMIT 1")
    suspend fun getTranslation(sourceText: String, sourceLang: String, targetLang: String): TranslationEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTranslation(translation: TranslationEntity)
    
    @Query("SELECT * FROM translations ORDER BY timestamp DESC")
    fun getAllTranslations(): Flow<List<TranslationEntity>>
    
    @Delete
    suspend fun deleteTranslation(translation: TranslationEntity)
    
    @Query("SELECT * FROM translations WHERE (:sourceLang IS NULL OR sourceLanguage = :sourceLang) AND (:targetLang IS NULL OR targetLanguage = :targetLang) ORDER BY timestamp DESC")
    fun getTranslationsFiltered(sourceLang: String?, targetLang: String?): Flow<List<TranslationEntity>>
    
    @Query("SELECT * FROM translations ORDER BY timestamp ASC")
    fun getAllTranslationsSortedByDateAsc(): Flow<List<TranslationEntity>>
    
    @Query("SELECT * FROM translations ORDER BY sourceText ASC")
    fun getAllTranslationsSortedByTextAsc(): Flow<List<TranslationEntity>>
    
    @Query("SELECT * FROM translations ORDER BY sourceText DESC")
    fun getAllTranslationsSortedByTextDesc(): Flow<List<TranslationEntity>>
}

