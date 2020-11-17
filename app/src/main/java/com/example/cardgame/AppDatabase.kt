package com.example.cardgame

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(User::class),version = 3)
abstract class AppDatabase : RoomDatabase() {

    abstract val userDao : UserDao


    companion object {
        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getInstance(context: Context) : AppDatabase{
            synchronized(this){

            var instance = INSTANCE

            if (instance == null ) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "user_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
            }

            return instance
        }
        }

    }

}