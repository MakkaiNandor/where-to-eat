package com.example.androidproject.database

import androidx.lifecycle.LiveData
import com.example.androidproject.database.entity.User

class DbRepository(private val dbDao: DbDao) {

    val allUsers: LiveData<List<User>> = dbDao.allUsers()

    fun getUser(email: String) = dbDao.getUser(email)

    fun checkUserForLogin(email: String, password: String) = dbDao.checkUserForLogin(email, password)

    fun checkUserForRegister(email: String) = dbDao.checkUserForRegister(email)

    suspend fun addUser(user: User){
        dbDao.addUser(user)
    }

    suspend fun updateUser(user: User){
        dbDao.updateUser(user)
    }

}