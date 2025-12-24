package com.task1_2
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bouquets")
data class BouquetEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "bouquet_id")
    val bouquetId: Long = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "is_available")
    var isAvailable: Boolean = true
)