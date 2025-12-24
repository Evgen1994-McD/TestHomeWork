package com.task1.utils

import com.task1.dao.FlowerShopDao
import com.task1.entity.BouquetEntity
import com.task1.entity.FlowerBouquetEntity
import com.task1.entity.FlowerEntity

const val ROMANTIC = "Романтический"
const val STANDART = "Стандартный"
const val WEDDING = "Свадебный"
const val CHEERFUL = "Веселый"

/**
 * Функция стартовой настройки
 */
 suspend fun initializeDatabase(dao: FlowerShopDao) {
    /**
     * Добавим цветы
     */

    val Rose = dao.insertFlower(
        FlowerEntity(
            flowerName = "Роза",
            availableCount = 100
        )
    )
    val Tulip = dao.insertFlower(
        FlowerEntity(
            flowerName = "Тюльпан",
            availableCount = 100
        )
    )
    val Daisy = dao.insertFlower(
        FlowerEntity(
            flowerName = "Маргаритка",
            availableCount = 100
        )
    )
    val Sunflower = dao.insertFlower(
        FlowerEntity(
            flowerName = "Подсолнух",
            availableCount = 100
        )
    )
    val Lily = dao.insertFlower(
        FlowerEntity(
            flowerName = "Лилия",
            availableCount = 100
        )
    )
    val Orchid = dao.insertFlower(
        FlowerEntity(
            flowerName = "Орхидея",
            availableCount = 100
        )
    )
    val Violet = dao.insertFlower(
        FlowerEntity(
            flowerName = "Фиалка",
            availableCount = 100
        )
    )
    val Hyacinth = dao.insertFlower(
        FlowerEntity(
            flowerName = "Гиацинт",
            availableCount = 100
        )
    )
    val Carnation = dao.insertFlower(
        FlowerEntity(
            flowerName = "Гвоздика",
            availableCount = 100
        )
    )
    val Poppy = dao.insertFlower(
        FlowerEntity(
            flowerName = "Мак",
            availableCount = 100
        )
    )

    /**
     * Добавим и настроим букеты
     */
    val romanticBouquet = dao.insertBouquet(BouquetEntity(bouquetName = ROMANTIC))

    dao.insertFlowerBouquet(
        FlowerBouquetEntity(
            bouquetId = romanticBouquet,
            flowerId = Rose,
            count = 3
        )
    )
    dao.insertFlowerBouquet(
        FlowerBouquetEntity(
        bouquetId = romanticBouquet,
        flowerId = Orchid,
        count = 3
    )
    )

    dao.insertFlowerBouquet(
        FlowerBouquetEntity(
        bouquetId = romanticBouquet,
        flowerId = Poppy,
        count = 7
    )
    )

    val standartBouquet = dao.insertBouquet(BouquetEntity(bouquetName = STANDART))

    dao.insertFlowerBouquet(
        FlowerBouquetEntity(
            bouquetId = romanticBouquet,
            flowerId = Rose,
            count = 8
        )
    )
    dao.insertFlowerBouquet(
        FlowerBouquetEntity(
            bouquetId = romanticBouquet,
            flowerId = Hyacinth,
            count = 3
        )
    )

    dao.insertFlowerBouquet(
        FlowerBouquetEntity(
            bouquetId = romanticBouquet,
            flowerId = Violet,
            count = 7
        )
    )

    val weddingBouquet = dao.insertBouquet(BouquetEntity(bouquetName = WEDDING))

    dao.insertFlowerBouquet(
        FlowerBouquetEntity(
            bouquetId = romanticBouquet,
            flowerId = Sunflower,
            count = 12
        )
    )
    dao.insertFlowerBouquet(
        FlowerBouquetEntity(
            bouquetId = romanticBouquet,
            flowerId = Lily,
            count = 1
        )
    )

    dao.insertFlowerBouquet(
        FlowerBouquetEntity(
            bouquetId = romanticBouquet,
            flowerId = Daisy,
            count = 4
        )
    )

    val cheerfulBouquet = dao.insertBouquet(BouquetEntity(bouquetName = CHEERFUL))

    dao.insertFlowerBouquet(
        FlowerBouquetEntity(
            bouquetId = romanticBouquet,
            flowerId = Carnation,
            count = 5
        )
    )
    dao.insertFlowerBouquet(
        FlowerBouquetEntity(
            bouquetId = romanticBouquet,
            flowerId = Lily,
            count = 1
        )
    )

    dao.insertFlowerBouquet(
        FlowerBouquetEntity(
            bouquetId = romanticBouquet,
            flowerId = Tulip,
            count = 2
        )
    )



}