package com.example.androidproject.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(tableName = "user_image_table", primaryKeys = ["userId", "restaurantId", "image"],
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
data class UserImage(
        val userId: String,
        val restaurantId: Long,
        val image: ByteArray
) {
        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as UserImage

                if (userId != other.userId) return false
                if (restaurantId != other.restaurantId) return false
                if (!image.contentEquals(other.image)) return false

                return true
        }

        override fun hashCode(): Int {
                var result = userId.hashCode()
                result = 31 * result + restaurantId.hashCode()
                result = 31 * result + image.contentHashCode()
                return result
        }
}