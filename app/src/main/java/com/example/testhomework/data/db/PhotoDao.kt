package com.example.testhomework.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PhotoDao {

    @Query("SELECT * FROM photo_album WHERE UPPER(title) LIKE '%' || UPPER(:params) || '%'  ")
    fun getAllPhotosByTitle(params: String): List<PhotoEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun savePhoto(photoEntity: PhotoEntity): Long

    @Query("SELECT * FROM photo_album WHERE id = :id LIMIT 1")
    fun getPhotoById(id:String):PhotoEntity

    @Query("SELECT * FROM photo_album")
    fun getAllPhotos(): List<PhotoEntity>





}