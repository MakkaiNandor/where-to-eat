package com.example.androidproject.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User (
        var name: String,
        @PrimaryKey val email: String,
        var address: String,
        var phone: String,
        val password: String
)