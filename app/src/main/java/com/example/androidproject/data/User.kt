package com.example.androidproject.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.androidproject.retrofit.model.Restaurant

@Entity(tableName = "user_table")
data class User (
    val name: String,
    @PrimaryKey
    val email: String,
    val address: String,
    val phone: String,
    val password: String
)