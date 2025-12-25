package com.task1

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration {
    companion object{
        val migration_1_2 = object : Migration(1, 2){
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE flowers ADD COLUMN country TEXT NOT NULL DEFAULT 'undefined'")
            }
        }

        val migration_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE bouquets ADD COLUMN decoration TEXT NOT NULL DEFAULT 'No decorations'")
            }

        }

}

}