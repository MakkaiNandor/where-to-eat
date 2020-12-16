package com.example.androidproject.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.androidproject.database.entity.User

@Database(entities = [User::class], version = 2, exportSchema = false)
abstract class Database : RoomDatabase() {

    abstract fun userDao(): DbDao

    companion object {
        @Volatile
        private var INSTANCE : com.example.androidproject.database.Database? = null

        /**
         * Create new UserDatabase instance. This class is singleton.
         *
         * @param context The context
         * @return The UserDatabase instance
         */
        fun getDatabase(context: Context): com.example.androidproject.database.Database {
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Database::class.java,
                    "user_database"
                ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
                INSTANCE = instance
                return instance
            }
        }
    }

}