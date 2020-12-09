package com.example.androidproject.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.androidproject.model.Restaurant

@Entity(tableName = "user_table")
data class User (
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val name : String,
    val address : String,
    val phone : String,
    val password : String,
    val favoriteRestaurants : List<Restaurant>
)