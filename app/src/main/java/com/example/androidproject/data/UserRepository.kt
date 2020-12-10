package com.example.androidproject.data

import androidx.lifecycle.LiveData

class UserRepository(private val userDao: UserDao) {

    val allUsers: LiveData<List<User>> = userDao.allUsers()

    suspend fun addUser(user: User){
        userDao.addUser(user)
    }

    suspend fun updateUser(user: User){
        userDao.updateUser(user)
    }

}