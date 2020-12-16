package com.example.androidproject.database

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.androidproject.database.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DbViewModel(application: Application) : AndroidViewModel(application) {

    private val allUsers: LiveData<List<User>>
    private val repository: DbRepository
    var loggedInUser: User? = null

    init {
        val userDao = Database.getDatabase(application).userDao()
        repository = DbRepository(userDao)
        allUsers = repository.allUsers
    }

    fun addUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }
    }

    fun setupLoggedInUser(email: String) {
        loggedInUser = repository.getUser(email).value
    }

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