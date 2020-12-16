package com.example.androidproject.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User (
        val name: String,
        @PrimaryKey val email: String,
        val address: String,
        val phone: String,
        val password: String
)