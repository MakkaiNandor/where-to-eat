package com.example.androidproject.api

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class ApiViewModel(private val repository: ApiRepository): ViewModel() {

    private var restaurantResponse: MutableLiveData<Response<Restaurant>> = MutableLiveData()
    val restaurantsResponse: MutableLiveData<Response<Restaurants>> = MutableLiveData()
    private var countriesResponse: MutableLiveData<Response<Countries>> = MutableLiveData()
    val citiesResponse: MutableLiveData<Response<Cities>> = MutableLiveData()

    fun getRestaurantById(id: Long){
        viewModelScope.launch(Dispatchers.IO) {
            restaurantResponse.postValue(repository.getRestaurantById(id))
        }
    }

    fun getAllRestaurants(filters: Map<String, String>) {
        viewModelScope.launch(Dispatchers.IO) {
            restaurantsResponse.postValue(repository.getAllRestaurants(filters))
        }
    }

    fun getCountries() {
        viewModelScope.launch(Dispatchers.IO) {
            countriesResponse.postValue(repository.getCountries())
        }
    }

    fun getCities() {
        viewModelScope.launch(Dispatchers.IO) {
            citiesResponse.postValue(repository.getCities())
        }
    }

}