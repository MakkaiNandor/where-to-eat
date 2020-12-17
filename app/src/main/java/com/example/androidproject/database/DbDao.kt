package com.example.androidproject.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.androidproject.database.entity.*

@Dao
interface DbDao {

    // User
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: User)

    @Query("SELECT * FROM user_table")
    fun allUsers(): List<User>

    @Query("SELECT * FROM user_table WHERE email = :email")
    fun getUser(email: String): User

    @Query("SELECT count(*) FROM user_table WHERE email = :email AND password = :password")
    fun checkUserForLogin(email: String, password: String): Int

    @Query("SELECT count(*) FROM user_table WHERE email = :email")
    fun checkUserForRegister(email: String): Int

    @Update
    suspend fun updateUser(user: User)

    // Favorite
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRestaurant(restaurant: Favorite)

    @Query("DELETE FROM favorite_table WHERE id NOT IN (SELECT DISTINCT favoriteId FROM user_favorite_table)")
    suspend fun deleteUnusedRestaurants()

    // User's favorites
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUserFavorite(userFavorite: UserFavorite)

    // @Query("INSERT INTO user_favorite_table VALUES (:email, :restId)")
    // suspend fun addUserFavorite(email: String, restId: Long)

    /*@Query("SELECT * FROM user_favorite_table")
    suspend fun getAllUserFavorite(): MutableList<UserFavorite>

    @Insert
    suspend fun insertAllUserFavorite(list: List<UserFavorite>)

    @Query("DELETE FROM user_favorite_table")
    suspend fun deleteAllUserFavorite()

    @Transaction
    suspend fun addUserFavorite(userFavorite: UserFavorite){
        val allUserFavorite: MutableList<UserFavorite> = getAllUserFavorite()
        allUserFavorite.add(userFavorite)
        deleteAllUserFavorite()
        insertAllUserFavorite(allUserFavorite)
    }*/

    @Delete
    suspend fun removeUserFavorite(userFavorite: UserFavorite)

    @Query("SELECT * FROM favorite_table WHERE id IN (SELECT favoriteId FROM user_favorite_table WHERE userId = :userEmail)")
    fun getUserFavorites(userEmail: String): List<Favorite>

}