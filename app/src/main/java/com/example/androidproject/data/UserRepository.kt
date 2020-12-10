package com.example.androidproject.data

import androidx.lifecycle.LiveData

class UserRepository(private val userDao: UserDao) {

    val allUsers: LiveData<List<User>> = userDao.allUsers()

    fun getUser(email: String) = userDao.getUser(email)

    fun checkUserForLogin(email: String, password: String) = userDao.checkUserForLogin(email, password)

    fun checkUserForRegister(email: String) = userDao.checkUserForRegister(email)

    suspend fun addUser(user: User){
        userDao.addUser(user)
    }

    suspend fun updateUser(user: User){
        userDao.updateUser(user)
    }

}