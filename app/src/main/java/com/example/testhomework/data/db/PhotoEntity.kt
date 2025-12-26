package com.example.testhomework.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo_album")
data class PhotoEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val title: String,
    val thumbnailUrl: String,
    val fullUrl: String
)
