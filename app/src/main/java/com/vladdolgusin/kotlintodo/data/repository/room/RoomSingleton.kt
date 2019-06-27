package com.vladdolgusin.kotlintodo.data.repository.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.migration.Migration
import com.vladdolgusin.kotlintodo.data.entity.TaskDB


@Database(entities = arrayOf(TaskDB::class), version = 3)
abstract class RoomSingleton : RoomDatabase() {
    abstract fun taskDao(): TaskDAO

    companion object {
        private var INSTANCE: RoomSingleton? = null

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE task ADD COLUMN savedTime INTEGER DEFAULT 0 NOT NULL")
                database.execSQL("ALTER TABLE task ADD COLUMN setAlarm INTEGER DEFAULT 0 NOT NULL")
            }
        }

        private val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE task ADD COLUMN whenStartAlarm INTEGER DEFAULT 0 NOT NULL")
            }
        }

        fun getInstance(context: Context): RoomSingleton {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                        context,
                        RoomSingleton::class.java,
                        "roomdb")
                        .addMigrations(MIGRATION_1_2)
                        .addMigrations(MIGRATION_2_3)
                        .build()
            }
            return INSTANCE as RoomSingleton
        }
    }
}