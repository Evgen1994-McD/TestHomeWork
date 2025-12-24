package com.task1_2

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flowers")
data class FlowerEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "flower_id")
    val flowerId: Long = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "available_count")
    var availableCount: Int
)