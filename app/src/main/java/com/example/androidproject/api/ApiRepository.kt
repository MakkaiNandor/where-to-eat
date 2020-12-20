package com.example.androidproject.api

import com.example.androidproject.api.RetrofitInstance.api

class ApiRepository {

    suspend fun getRestaurantById(id: Long) = api.getRestaurantById(id)

    suspend fun getAllRestaurants(params: Map<String, String>) = api.getAllRestaurants(params)

    suspend fun getCountries() = api.getCountries()

    suspend fun getCities() =  api.getCities()

}