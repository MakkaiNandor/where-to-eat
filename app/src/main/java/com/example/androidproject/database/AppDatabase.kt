package com.example.androidproject.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.androidproject.database.entity.*

@Database(entities = [User::class, Restaurant::class, UserFavorite::class, UserImage::class], version = 6, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): DbDao

    companion object {
        @Volatile
        private var INSTANCE : AppDatabase? = null

        /**
         * Create new UserDatabase instance. This class is singleton.
         *
         * @param context The context
         * @return The UserDatabase instance
         */
        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
                INSTANCE = instance
                return instance
            }
        }
    }

}