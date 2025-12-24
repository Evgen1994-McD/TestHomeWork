package com.task1_2

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        FlowerEntity::class,
        BouquetEntity::class,
        FlowerBouquetEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FlowerDatabase : RoomDatabase() {
    abstract fun flowerShopDao(): FlowerShopDao

    companion object {
        private const val DATABASE_NAME = "flower_shop.db"
        
        @Volatile
        private var INSTANCE: FlowerDatabase? = null

        fun getDatabase(context: Context): FlowerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FlowerDatabase::class.java,
                    DATABASE_NAME
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Включаем foreign keys
                            db.execSQL("PRAGMA foreign_keys = ON")
                        }

                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            // Включаем foreign keys при каждом открытии
                            db.execSQL("PRAGMA foreign_keys = ON")
                        }
                    })
                    .build()
                INSTANCE = instance
                
                // Проверяем и заполняем данные, если база пуста
                // Это делается после создания instance, чтобы гарантировать его доступность
                CoroutineScope(Dispatchers.IO).launch {
                    checkAndFillInitialData(instance)
                }
                
                instance
            }
        }

        private suspend fun checkAndFillInitialData(database: FlowerDatabase) {
            val dao = database.flowerShopDao()
            // Проверяем, пуста ли база данных
            if (dao.isDatabaseEmpty()) {
                Log.d("FlowerDatabase", "База данных пуста, заполняем начальными данными...")
                fillInitialData(database)
            }
        }

        private suspend fun fillInitialData(database: FlowerDatabase) {
            val dao = database.flowerShopDao()

            // ===== СОЗДАЕМ 10 ВИДОВ ЦВЕТОВ =====
            val flowers = listOf(
                FlowerEntity(name = "Белая роза", availableCount = 100),
                FlowerEntity(name = "Красная роза", availableCount = 80),
                FlowerEntity(name = "Тюльпан", availableCount = 200),
                FlowerEntity(name = "Хризантема", availableCount = 150),
                FlowerEntity(name = "Гербера", availableCount = 120),
                FlowerEntity(name = "Лилии", availableCount = 70),
                FlowerEntity(name = "Пионы", availableCount = 90),
                FlowerEntity(name = "Орхидеи", availableCount = 50),
                FlowerEntity(name = "Подсолнухи", availableCount = 60),
                FlowerEntity(name = "Гвоздика", availableCount = 180)
            )

            // Вставляем цветы и сохраняем их ID
            val flowerIds = mutableListOf<Long>()
            flowers.forEach { flower ->
                flowerIds.add(dao.insertFlower(flower))
            }

            // ===== СОЗДАЕМ БУКЕТЫ =====

            // Букет 1: Романтический (3 белые розы, 10 красных роз, 2 тюльпана)
            val romanticBouquetId = dao.insertBouquet(
                BouquetEntity(name = "Романтический букет")
            )

            // Связываем цветы с букетом
            dao.insertFlowerBouquet(
                FlowerBouquetEntity(
                    bouquetId = romanticBouquetId,
                    flowerId = flowerIds[0], // Белая роза
                    count = 3
                )
            )
            dao.insertFlowerBouquet(
                FlowerBouquetEntity(
                    bouquetId = romanticBouquetId,
                    flowerId = flowerIds[1], // Красная роза
                    count = 10
                )
            )
            dao.insertFlowerBouquet(
                FlowerBouquetEntity(
                    bouquetId = romanticBouquetId,
                    flowerId = flowerIds[2], // Тюльпан
                    count = 2
                )
            )

            // Букет 2: Весенний
            val springBouquetId = dao.insertBouquet(
                BouquetEntity(name = "Весенний букет")
            )

            dao.insertFlowerBouquet(
                FlowerBouquetEntity(
                    bouquetId = springBouquetId,
                    flowerId = flowerIds[2], // Тюльпан
                    count = 5
                )
            )
            dao.insertFlowerBouquet(
                FlowerBouquetEntity(
                    bouquetId = springBouquetId,
                    flowerId = flowerIds[3], // Хризантема
                    count = 3
                )
            )

            // Букет 3: Свадебный
            val weddingBouquetId = dao.insertBouquet(
                BouquetEntity(name = "Свадебный букет")
            )

            dao.insertFlowerBouquet(
                FlowerBouquetEntity(
                    bouquetId = weddingBouquetId,
                    flowerId = flowerIds[0], // Белая роза
                    count = 15
                )
            )
            dao.insertFlowerBouquet(
                FlowerBouquetEntity(
                    bouquetId = weddingBouquetId,
                    flowerId = flowerIds[6], // Пионы
                    count = 7
                )
            )

            Log.d("FlowerDatabase", "Начальные данные успешно заполнены")
        }
    }
}
