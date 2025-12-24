package com.task1_2

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FlowerShopDao {

    // ===== ЦВЕТЫ =====
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlower(flower: FlowerEntity):Long

    @Query("SELECT * FROM flowers")
    fun getAllFlowers(): Flow<List<FlowerEntity>>

    @Query("SELECT * FROM flowers WHERE flower_id = :flowerId")
    suspend fun getFlowerById(flowerId: Long): FlowerEntity?

    @Update
    suspend fun updateFlower(flower: FlowerEntity)

    @Query("UPDATE flowers SET available_count = available_count - :count WHERE flower_id = :flowerId")
    suspend fun decreaseFlowerCount(flowerId: Long, count: Int)

    // Проверка, пуста ли база данных (нет цветов)
    @Query("SELECT COUNT(*) FROM flowers")
    suspend fun getFlowerCount(): Int

    suspend fun isDatabaseEmpty(): Boolean {
        return getFlowerCount() == 0
    }

    // ===== БУКЕТЫ =====
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBouquet(bouquet: BouquetEntity): Long

    @Query("SELECT * FROM bouquets")
    fun getAllBouquets(): Flow<List<BouquetEntity>>

    @Query("SELECT * FROM bouquets WHERE is_available = 1")
    fun getAvailableBouquets(): Flow<List<BouquetEntity>>

    @Query("SELECT * FROM bouquets WHERE bouquet_id = :bouquetId")
    suspend fun getBouquetById(bouquetId: Long): BouquetEntity?

    @Update
    suspend fun updateBouquet(bouquet: BouquetEntity)

    // ===== СВЯЗЬ ЦВЕТОВ И БУКЕТОВ =====
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlowerBouquet(flowerBouquet: FlowerBouquetEntity)

    @Query("SELECT * FROM flower_bouquet WHERE bouquet_id = :bouquetId")
    suspend fun getFlowerBouquetByBouquetId(bouquetId: Long): List<FlowerBouquetEntity>

    // ===== СЛОЖНЫЕ ЗАПРОСЫ =====
    @Query("""
        SELECT f.*, fb.count 
        FROM flower_bouquet fb
        JOIN flowers f ON fb.flower_id = f.flower_id
        WHERE fb.bouquet_id = :bouquetId
    """)
    suspend fun getFlowersForBouquet(bouquetId: Long): List<FlowerWithCount>

    // Главный метод: покупка букета
    @Transaction
    suspend fun purchaseBouquet(bouquetId: Long): PurchaseResult {
        // 1. Проверяем существование букета
        val bouquet = getBouquetById(bouquetId) ?:
        return PurchaseResult.Error("Букет не найден")

        if (!bouquet.isAvailable) {
            return PurchaseResult.Error("Букет недоступен")
        }

        // 2. Получаем состав букета
        val flowerComposition = getFlowersForBouquet(bouquetId)

        // 3. Проверяем наличие всех цветов
        for (flowerWithCount in flowerComposition) {
            if (flowerWithCount.flower.availableCount < flowerWithCount.count) {
                return PurchaseResult.Error(
                    "Недостаточно цветов: ${flowerWithCount.flower.name}. " +
                            "Нужно: ${flowerWithCount.count}, доступно: ${flowerWithCount.flower.availableCount}"
                )
            }
        }

        // 4. Уменьшаем количество цветов
        for (flowerWithCount in flowerComposition) {
            decreaseFlowerCount(flowerWithCount.flower.flowerId, flowerWithCount.count)
        }

        // 5. Помечаем букет как недоступный, если нужно
        // updateBouquet(bouquet.copy(isAvailable = false))

        return PurchaseResult.Success(bouquet)
    }
}

// Класс для результата запроса с количеством
data class FlowerWithCount(
    @Embedded
    val flower: FlowerEntity,
    val count: Int
)

// Результат покупки
sealed class PurchaseResult {
    data class Success(val bouquet: BouquetEntity) : PurchaseResult()
    data class Error(val message: String) : PurchaseResult()
}