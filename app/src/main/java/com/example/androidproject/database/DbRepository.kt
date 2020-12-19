package com.example.androidproject.database

import com.example.androidproject.database.entity.*

class DbRepository(private val dbDao: DbDao) {

    // User
    fun getUser(email: String) = dbDao.getUser(email)

    fun checkUserForLogin(email: String, password: String) = dbDao.checkUserForLogin(email, password)

    fun checkUserForRegister(email: String) = dbDao.checkUserForRegister(email)

    suspend fun addUser(user: User){
        dbDao.addUser(user)
    }

    suspend fun updateUser(user: User){
        dbDao.updateUser(user)
    }

    // Restaurant
    suspend fun addRestaurant(restaurant: Restaurant){
        dbDao.addRestaurant(restaurant)
    }

    suspend fun deleteUnusedRestaurants(){
        dbDao.deleteUnusedRestaurants()
    }

    // User's favorites
    suspend fun addUserFavorite(userFavorite: UserFavorite){
        dbDao.addUserFavorite(userFavorite)
    }

    suspend fun removeUserFavorite(userFavorite: UserFavorite){
        dbDao.removeUserFavorite(userFavorite)
    }

    fun getUserFavorites(userEmail: String) = dbDao.getUserFavorites(userEmail)

    // User's images per restaurant
    suspend fun addImageToRestaurant(userImage: UserImage){
        dbDao.addImageToRestaurant(userImage)
    }

    suspend fun removeImageFromRestaurant(userImage: UserImage){
        dbDao.removeImageFromRestaurant(userImage)
    }

    fun getUserImagesByRestaurant(userEmail: String, restaurantId: Long) = dbDao.getUserImagesByRestaurant(userEmail, restaurantId)

}