package com.task1.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flowers")
data class FlowerEntity(
    @PrimaryKey(autoGenerate = true)
    val flowerId:Long=0,
    val flowerName:String,
    var availableCount: Int // Сколько доступно на складе
)


