package com.example.androidproject.api

import com.example.androidproject.api.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @GET("/restaurants/{id}")
    suspend fun getRestaurantById(@Path("id") id: Long): Response<Restaurant>

    @GET("/restaurants")
    suspend fun getAllRestaurants(@QueryMap params: Map<String, String>): Response<Restaurants>

    @GET("/countries")
    suspend fun getCountries(): Response<Countries>

    @GET("/cities")
    suspend fun getCities(): Response<Cities>
}