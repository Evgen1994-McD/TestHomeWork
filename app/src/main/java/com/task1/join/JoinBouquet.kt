package com.task1.join

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.task1.entity.BouquetEntity
import com.task1.entity.FlowerBouquetEntity
import com.task1.entity.FlowerEntity

data class JoinBouquet(
    @Embedded
    val bouquet: BouquetEntity,

    @Relation(
        parentColumn = "bouquetId",
        entityColumn = "flowerId",
        associateBy = Junction(
            value = FlowerBouquetEntity::class,
            parentColumn = "bouquet_id",
            entityColumn = "flower_id"
        )
    )
    val flowers: List<FlowerWithCount> // Список цветов с количеством
)

// Цвет + количество в конкретном букете
data class FlowerWithCount(
    @Embedded
    val flower: FlowerEntity,
    @ColumnInfo(name = "count")
    val count: Int
)