package com.example.androidproject

import com.example.androidproject.model.Restaurant
import com.example.androidproject.model.Restaurants
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @GET("/api/restaurants/{id}")
    fun getRestaurant(@Path("id") id: Int): Call<Restaurant>

    @GET("/api/restaurants")
    fun getRestaurants(@QueryMap params: Map<String, String>): Call<Restaurants>
}