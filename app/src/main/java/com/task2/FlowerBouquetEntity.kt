package com.task2


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "flower_bouquet",
    primaryKeys = ["bouquet_id", "flower_id"],
    foreignKeys = [
        ForeignKey(
            entity = BouquetEntity::class,
            parentColumns = ["bouquet_id"],
            childColumns = ["bouquet_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = FlowerEntity::class,
            parentColumns = ["flower_id"],
            childColumns = ["flower_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("flower_id"),
        Index("bouquet_id")
    ]
)
data class FlowerBouquetEntity(
    @ColumnInfo(name = "bouquet_id")
    val bouquetId: Long,

    @ColumnInfo(name = "flower_id")
    val flowerId: Long,

    @ColumnInfo(name = "count")
    val count: Int
)