package com.example.androidproject.retrofit

import com.example.androidproject.model.*
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @GET("/api/restaurants/{id}")
    fun getRestaurant(@Path("id") id: Int): Call<Restaurant>

    @GET("/api/restaurants")
    fun getRestaurants(@QueryMap params: Map<String, String>): Call<Restaurants>

    @GET("/api/countries")
    fun getCountries(): Call<Countries>

    @GET("/api/cities")
    fun getCities(): Call<Cities>
}