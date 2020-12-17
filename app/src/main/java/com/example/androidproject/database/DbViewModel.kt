package com.example.androidproject.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.model.Restaurant
import com.example.androidproject.database.entity.Favorite
import com.example.androidproject.database.entity.User
import com.example.androidproject.database.entity.UserFavorite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DbViewModel(application: Application) : AndroidViewModel(application) {

    private val allUsers: List<User>
    private val repository: DbRepository
    var loggedInUser: User? = null

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        repository = DbRepository(userDao)
        allUsers = repository.allUsers
    }

    fun addUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }
    }

    fun setupLoggedInUser(email: String) {
        loggedInUser = repository.getUser(email)
    }

    fun checkUserForLogin(email: String, password: String) = repository.checkUserForLogin(email, password)

    fun checkUserForRegister(email: String) = repository.checkUserForRegister(email)

    fun updateUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(user)
        }
    }

    fun addUserFavorite(restaurant: Restaurant){
        viewModelScope.launch(Dispatchers.IO) {
            if(loggedInUser != null) {
                repository.addRestaurant(restaurantToFavorite(restaurant))
                repository.addUserFavorite(UserFavorite(loggedInUser!!.email, restaurant.id))
            }
        }
    }

    fun removeUserFavorite(restaurant: Restaurant){
        viewModelScope.launch(Dispatchers.IO) {
            if(loggedInUser != null){
                repository.removeUserFavorite(UserFavorite(loggedInUser!!.email, restaurant.id))
            }
        }
    }

    fun getUserFavorites(): List<Restaurant> {
        return repository.getUserFavorites(loggedInUser!!.email).map {
            Restaurant(
                    it.id,
                    it.name,
                    it.address,
                    it.city,
                    it.state,
                    it.area,
                    it.postal_code,
                    it.country,
                    it.phone,
                    it.lat,
                    it.lng,
                    it.price,
                    it.reserve_url,
                    it.mobile_reserve_url,
                    it.image_url
            )
        }
    }

    private fun restaurantToFavorite(restaurant: Restaurant): Favorite {
        return Favorite(
                restaurant.id,
                restaurant.name,
                restaurant.address,
                restaurant.city,
                restaurant.state,
                restaurant.area,
                restaurant.postal_code,
                restaurant.country,
                restaurant.phone,
                restaurant.lat,
                restaurant.lng,
                restaurant.price,
                restaurant.reserve_url,
                restaurant.mobile_reserve_url,
                restaurant.image_url
        )
    }

}