package com.example.androidproject.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.androidproject.database.entity.*

@Dao
interface DbDao {

    // User
    @Insert(onConflict = OnConflictStrategy.ABORT)
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

    // Favorite
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addRestaurant(restaurant: Favorite)

    // User's favorites
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addUserFavorite(userFavorite: UserFavorite)

    @Query("SELECT * FROM favorite_table WHERE id IN (SELECT favoriteId FROM user_favorite_table WHERE userId = :userEmail)")
    fun getUserFavorites(userEmail: String): List<Favorite>

}