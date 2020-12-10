package com.example.androidproject.api

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.api.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class DataViewModel(private val repository: ApiRepository): ViewModel() {

    val restaurantResponse: MutableLiveData<Response<Restaurant>> = MutableLiveData()
    val restaurantsResponse: MutableLiveData<Response<Restaurants>> = MutableLiveData()
    val countriesResponse: MutableLiveData<Response<Countries>> = MutableLiveData()
    val citiesResponse: MutableLiveData<Response<Cities>> = MutableLiveData()

    fun getRestaurantById(id: Int){
        viewModelScope.launch {
            restaurantResponse.value = repository.getRestaurantById(id)
        }
    }

    fun getAllRestaurants(params: Map<String, String>) {
        viewModelScope.launch {
            restaurantsResponse.value = repository.getAllRestaurants(params)
        }
    }

    fun getCountries() {
        viewModelScope.launch {
            countriesResponse.value = repository.getCountries()
        }
    }

    fun getCities() {
        viewModelScope.launch {
            citiesResponse.value = repository.getCities()
        }
    }

}