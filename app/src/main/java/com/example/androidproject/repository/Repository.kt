package com.example.androidproject.repository

import com.example.androidproject.api.RetrofitInstance
import com.example.androidproject.model.Restaurant
import retrofit2.Response

class Repository {

    suspend fun getRestaurant(id: Int): Response<Restaurant> {
        return RetrofitInstance.api.getRestaurant(id)
    }

}