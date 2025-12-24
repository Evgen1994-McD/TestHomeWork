package com.task1.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "bouquet_flowers",
    primaryKeys = ["bouquetId", "flowerId"],
    foreignKeys = [
        ForeignKey(
            entity = BouquetEntity::class,
            parentColumns = ["bouquetId"],
            childColumns = ["bouquetId"],
            onDelete = ForeignKey.CASCADE
        ),
    ForeignKey(
        entity = FlowerEntity::class,
        parentColumns = ["flowerId"],
        childColumns = ["flowerId"],
        onDelete = ForeignKey.CASCADE
    )
    ],
    indices = [
        Index("bouquetId"),
        Index("flowerId")
    ]
)
data class FlowerBouquetEntity(
    val bouquetId:Long,
    val flowerId:Long,
    val count:Int

)
