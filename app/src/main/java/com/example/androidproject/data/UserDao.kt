package com.example.androidproject.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Query("SELECT * FROM user_table")
    fun allUsers(): LiveData<List<User>>

    @Query("SELECT * FROM user_table WHERE email = :email")
    fun getUser(email: String): LiveData<User>

    @Query("SELECT count(*) FROM user_table WHERE email = :email AND password = :password")
    fun checkUserForLogin(email: String, password: String): Int

    @Query("SELECT count(*) FROM user_table WHERE email = :email")
    fun checkUserForRegister(email: String): Int

    @Update
    suspend fun updateUser(user: User)

}