package com.task1.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.task1.entity.BouquetEntity
import com.task1.entity.FlowerBouquetEntity
import com.task1.entity.FlowerEntity
import com.task1.join.FlowerWithCount
import com.task1.join.JoinBouquet

@Dao

interface FlowerShopDao {



    // --- Цветы ---
    @Query("SELECT * FROM flowers")
    suspend fun getAllFlowers(): List<FlowerEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertFlower(flower: FlowerEntity):Long

    @Update
    suspend fun updateFlower(flower: FlowerEntity)

    @Insert
    suspend fun insertBouquet(bouquetEntity: BouquetEntity):Long

    @Query("UPDATE flowers SET availableCount = availableCount - :count WHERE flowerId = :flowerId")
    suspend fun decreaseFlowerCount(flowerId: Long, count: Int)

    // --- Букеты ---
    @Transaction
    @Query("SELECT * FROM bouquets WHERE isAvailable = 1")
    suspend fun getAvailableBouquets(): List<JoinBouquet>

    @Query("SELECT * FROM bouquets WHERE bouquetId = :bouquetId")
    suspend fun getBouquetById(bouquetId: Long): JoinBouquet?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlowerBouquet(flowerBouquetEntity: FlowerBouquetEntity):Long

    // --- Покупка букета (важнейший метод!) ---
    @Transaction
    suspend fun purchaseBouquet(bouquetId: Long): Boolean {
        // 1. Получаем букет со всеми цветами
        val bouquet = getBouquetById(bouquetId) ?: return false

        // 2. Проверяем, есть ли все цветы в достаточном количестве
        for (flowerWithCount in bouquet.flowers) {
            if (flowerWithCount.flower.availableCount < flowerWithCount.count) {
                return false // Не хватает какого-то цветка
            }
        }

        // 3. Уменьшаем количество доступных цветов
        for (flowerWithCount in bouquet.flowers) {
            decreaseFlowerCount(flowerWithCount.flower.flowerId, flowerWithCount.count)
        }

        // 4. (Опционально) Помечаем букет как недоступный, если был в единственном экземпляре
        // updateBouquetAvailability(bouquetId)

        return true
    }

    // Получить состав букета (цветы + количество)
    @Query("""
        SELECT f.*, bfc.count as count 
        FROM bouquet_flowers bfc
        JOIN flowers f ON f.flowerId = bfc.flowerId
        WHERE bfc.bouquetId = :bouquetId
    """)
    suspend fun getFlowersForBouquet(bouquetId: Long): List<FlowerWithCount>
}