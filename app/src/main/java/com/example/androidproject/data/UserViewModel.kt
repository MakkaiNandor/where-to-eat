package com.example.androidproject.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val allUsers: LiveData<List<User>>
    private val repository: UserRepository

    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
        allUsers = repository.allUsers
    }

    fun addUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }
    }

    private fun getUser(email: String) = repository.getUser(email)

    fun checkUserForLogin(email: String, password: String): Int {
        val count = repository.checkUserForLogin(email, password)
        Log.d("User", "$count")
        return count
    }

    fun checkUserForRegister(email: String): Int {
        val count = repository.checkUserForRegister(email)
        Log.d("User", "$count")
        return count
    }

    fun updateUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(user)
        }
    }

}