package com.example.androidproject.database.entity

import androidx.room.*

@Entity(tableName = "user_favorite_table", primaryKeys = ["userId", "favoriteId"],
    indices = [ Index(value = ["userId"]), Index(value = ["favoriteId"]) ],
    foreignKeys = [
        ForeignKey(
                entity = User::class,
                parentColumns = ["email"],
                childColumns = ["userId"],
                onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
                entity = Favorite::class,
                parentColumns = ["id"],
                childColumns = ["favoriteId"],
                onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserFavorite(
        val userId: String,
        val favoriteId: Long
)

