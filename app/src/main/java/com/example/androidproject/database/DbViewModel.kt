package com.example.androidproject.database

import android.app.Application
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.activity.MainActivity
import com.example.androidproject.adapter.ImageAdapter
import com.example.androidproject.database.entity.User
import com.example.androidproject.database.entity.UserFavorite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.androidproject.database.entity.Restaurant as Favorite
import com.example.androidproject.api.model.Restaurant
import com.example.androidproject.database.entity.UserImage

class DbViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DbRepository

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        repository = DbRepository(userDao)
    }

    // User
    fun getUser(email: String) = repository.getUser(email)

    fun checkUserForLogin(email: String, password: String) = repository.checkUserForLogin(email, password)

    fun checkUserForRegister(email: String) = repository.checkUserForRegister(email)

    fun addUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }
    }

    fun updateUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(user)
        }
    }

    // User's favorites
    fun addUserFavorite(restaurant: Restaurant){
        viewModelScope.launch(Dispatchers.IO) {
            if(MainActivity.loggedInUser != null) {
                if(repository.checkRestaurant(restaurant.id) == 0) {
                    repository.addRestaurant(restaurantToFavorite(restaurant))
                }
                repository.addUserFavorite(UserFavorite(MainActivity.loggedInUser!!.email, restaurant.id))
            }
        }
    }

    fun removeUserFavorite(restaurantId: Long){
        viewModelScope.launch(Dispatchers.IO) {
            if(MainActivity.loggedInUser != null){
                repository.removeUserFavorite(UserFavorite(MainActivity.loggedInUser!!.email, restaurantId))
                repository.deleteUnusedRestaurants()
            }
        }
    }

    fun getUserFavorites(): List<Restaurant> {
        if(MainActivity.loggedInUser != null) {
            return repository.getUserFavorites(MainActivity.loggedInUser!!.email).map {
                Restaurant(
                        it.id, it.name, it.address, it.city, it.state, it.area, it.postal_code, it.country,
                        it.phone, it.lat, it.lng, it.price, it.reserve_url, it.mobile_reserve_url, it.image_url
                )
            }
        }
        return listOf()
    }

    // User's images per restaurant
    fun addImageToRestaurant(restaurant: Restaurant, image: ByteArray){
        viewModelScope.launch(Dispatchers.IO) {
            if(MainActivity.loggedInUser != null){
                if(repository.checkRestaurant(restaurant.id) == 0) {
                    repository.addRestaurant(restaurantToFavorite(restaurant))
                }
                repository.addImageToRestaurant(UserImage(MainActivity.loggedInUser!!.email, restaurant.id, image))
            }
        }
    }

    fun removeImageFromRestaurant(restaurantId: Long, image: ByteArray){
        viewModelScope.launch(Dispatchers.IO) {
            if(MainActivity.loggedInUser != null) {
                repository.removeImageFromRestaurant(UserImage(MainActivity.loggedInUser!!.email, restaurantId, image))
                repository.deleteUnusedRestaurants()
            }
        }
    }

    fun getUserImagesByRestaurant(restaurantId: Long): List<ByteArray> {
        if(MainActivity.loggedInUser != null){
            return repository.getUserImagesByRestaurant(MainActivity.loggedInUser!!.email, restaurantId)
        }
        return listOf()
    }

    fun deleteAllUserImages() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllUserImages()
        }
    }

    private fun restaurantToFavorite(restaurant: Restaurant): Favorite = Favorite(
            restaurant.id, restaurant.name, restaurant.address, restaurant.city,
            restaurant.state, restaurant.area, restaurant.postal_code, restaurant.country,
            restaurant.phone, restaurant.lat, restaurant.lng, restaurant.price,
            restaurant.reserve_url, restaurant.mobile_reserve_url, restaurant.image_url
    )

}