package com.example.androidproject.api

import com.example.androidproject.api.RetrofitInstance.api
import com.example.androidproject.api.model.*
import retrofit2.Response

class ApiRepository {

    suspend fun getRestaurantById(id: Int): Response<Restaurant> {
        return api.getRestaurantById(id)
    }

    suspend fun getAllRestaurants(params: Map<String, String>):  Response<Restaurants> {
        return api.getAllRestaurants(params)
    }

    suspend fun getCountries():  Response<Countries> {
        return api.getCountries()
    }

    suspend fun getCities():  Response<Cities> {
        return api.getCities()
    }

}