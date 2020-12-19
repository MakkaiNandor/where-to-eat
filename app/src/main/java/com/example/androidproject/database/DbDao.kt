package com.example.androidproject.database

import androidx.room.*
import com.example.androidproject.database.entity.*

@Dao
interface DbDao {

    // User
    @Query("SELECT * FROM user_table WHERE email = :email")
    fun getUser(email: String): User

    @Query("SELECT count(*) FROM user_table WHERE email = :email AND password = :password")
    fun checkUserForLogin(email: String, password: String): Int

    @Query("SELECT count(*) FROM user_table WHERE email = :email")
    fun checkUserForRegister(email: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    // Restaurant
    @Query("SELECT count(*) FROM restaurant_table WHERE id = :id")
    suspend fun checkRestaurant(id: Long): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRestaurant(restaurant: Restaurant)

    @Query("DELETE FROM restaurant_table WHERE id NOT IN (SELECT DISTINCT restaurantId FROM user_favorite_table) AND  id NOT IN (SELECT DISTINCT restaurantId FROM user_image_table)")
    suspend fun deleteUnusedRestaurants()

    // User's favorites
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUserFavorite(userFavorite: UserFavorite)

    @Delete
    suspend fun removeUserFavorite(userFavorite: UserFavorite)

    @Query("SELECT * FROM restaurant_table WHERE id IN (SELECT restaurantId FROM user_favorite_table WHERE userId = :userEmail)")
    fun getUserFavorites(userEmail: String): List<Restaurant>

    // User's images per restaurant
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addImageToRestaurant(userImage: UserImage)

    @Delete
    suspend fun removeImageFromRestaurant(userImage: UserImage)

    @Query("SELECT image FROM user_image_table WHERE userId = :userEmail AND restaurantId = :restaurantId")
    fun getUserImagesByRestaurant(userEmail: String, restaurantId: Long): List<ByteArray>

    @Query("DELETE FROM user_image_table")
    suspend fun deleteAllUserImages()

}