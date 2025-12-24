package com.task1

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.task1.dao.FlowerShopDao
import com.task1.entity.BouquetEntity
import com.task1.entity.FlowerBouquetEntity
import com.task1.entity.FlowerEntity
import com.task1.join.JoinBouquet
import com.task1.utils.initializeDatabase
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

        @Volatile
        private var INSTANCE: FlowerDatabase? = null


        fun getDatabase(context: Context): FlowerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FlowerDatabase::class.java,
                    "flower_bouquet.db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                getDatabase(context).flowerShopDao().apply {
                                    initializeDatabase(flowerShopDao())
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }


}