package com.example.androidproject.database

import androidx.lifecycle.LiveData
import com.example.androidproject.database.entity.Favorite
import com.example.androidproject.database.entity.User
import com.example.androidproject.database.entity.UserFavorite

class DbRepository(private val dbDao: DbDao) {

    val allUsers: List<User> = dbDao.allUsers()

    fun getUser(email: String) = dbDao.getUser(email)

    fun checkUserForLogin(email: String, password: String) = dbDao.checkUserForLogin(email, password)

    fun checkUserForRegister(email: String) = dbDao.checkUserForRegister(email)

    suspend fun addUser(user: User){
        dbDao.addUser(user)
    }

    suspend fun updateUser(user: User){
        dbDao.updateUser(user)
    }

    suspend fun addRestaurant(restaurant: Favorite){
        dbDao.addRestaurant(restaurant)
    }

    suspend fun deleteUnusedRestaurants(){
        dbDao.deleteUnusedRestaurants()
    }

    suspend fun addUserFavorite(userFavorite: UserFavorite){
        dbDao.addUserFavorite(userFavorite)
    }

    suspend fun removeUserFavorite(userFavorite: UserFavorite){
        dbDao.removeUserFavorite(userFavorite)
    }

    fun getUserFavorites(userEmail: String) = dbDao.getUserFavorites(userEmail)

}