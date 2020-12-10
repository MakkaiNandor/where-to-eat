package com.example.androidproject.retrofit

import com.example.androidproject.retrofit.model.*
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @GET("/restaurants/{id}")
    fun getRestaurant(@Path("id") id: Int): Call<Restaurant>

    @GET("/restaurants")
    fun getRestaurants(@QueryMap params: Map<String, String>): Call<Restaurants>

    @GET("/countries")
    fun getCountries(): Call<Countries>

    @GET("/cities")
    fun getCities(): Call<Cities>
}