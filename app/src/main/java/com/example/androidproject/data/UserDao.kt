package com.example.androidproject.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Query("SELECT * FROM user_table")
    fun allUsers(): LiveData<List<User>>

    @Update
    suspend fun updateUser(user: User)

}