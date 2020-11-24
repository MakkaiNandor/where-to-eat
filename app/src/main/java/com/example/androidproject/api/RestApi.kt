package com.example.androidproject.api

import com.example.androidproject.model.Restaurant
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RestApi {

    @GET("api/restaurants/{id}")
    suspend fun getRestaurant(@Path("id") id: Int): Response<Restaurant>
}