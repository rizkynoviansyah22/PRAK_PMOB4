package com.example.post4_364

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [Wni::class], version = 1)
abstract class DatabaseWni : RoomDatabase() {
    abstract fun wniDao(): WniDao

    companion object {
        @Volatile
        private var INSTANCE: DatabaseWni? = null

        fun getDatabase(context: Context): DatabaseWni {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseWni::class.java,
                    "wni_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
