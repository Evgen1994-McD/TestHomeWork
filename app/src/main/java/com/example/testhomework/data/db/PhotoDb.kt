package com.example.testhomework.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [PhotoEntity::class],
    version = 1
)
abstract class PhotoDb:RoomDatabase() {
    abstract fun photoDao():PhotoDao



}