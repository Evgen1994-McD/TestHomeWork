package com.task1.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bouquets")
data class BouquetEntity(
    @PrimaryKey(autoGenerate = true)
    val bouquetId:Long=0,
    val bouquetName:String,
    var isAvailable: Boolean = true // Доступен ли для покупки
)