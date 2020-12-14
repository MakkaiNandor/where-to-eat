package com.example.androidproject.api

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class DataViewModel(private val repository: ApiRepository): ViewModel() {

    //val restaurantResponse: MutableLiveData<Response<Restaurant>> = MutableLiveData()
    val restaurantsResponse: MutableLiveData<Response<Restaurants>> = MutableLiveData()
    //val countriesResponse: MutableLiveData<Response<Countries>> = MutableLiveData()
    val citiesResponse: MutableLiveData<Response<Cities>> = MutableLiveData()

    /*fun getRestaurantById(id: Long){
        viewModelScope.launch(Dispatchers.IO) {
            restaurantResponse.postValue(repository.getRestaurantById(id))
        }
    }*/

    fun getAllRestaurants(params: Map<String, String>) {
        viewModelScope.launch(Dispatchers.IO) {
            restaurantsResponse.postValue(repository.getAllRestaurants(params))
        }
    }

    /*fun getCountries() {
        viewModelScope.launch(Dispatchers.IO) {
            countriesResponse.postValue(repository.getCountries())
        }
    }*/

    fun getCities() {
        viewModelScope.launch(Dispatchers.IO) {
            citiesResponse.postValue(repository.getCities())
        }
    }

}