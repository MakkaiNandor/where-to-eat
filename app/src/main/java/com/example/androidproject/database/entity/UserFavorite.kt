package com.example.androidproject.database.entity

import androidx.room.*

@Entity(tableName = "user_favorite_table", primaryKeys = ["userId", "restaurantId"],
    indices = [ Index(value = ["userId"]), Index(value = ["restaurantId"]) ],
    foreignKeys = [
        ForeignKey(
                entity = User::class,
                parentColumns = ["email"],
                childColumns = ["userId"],
                onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
                entity = Restaurant::class,
                parentColumns = ["id"],
                childColumns = ["restaurantId"],
                onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserFavorite(
        val userId: String,
        val restaurantId: Long
)

