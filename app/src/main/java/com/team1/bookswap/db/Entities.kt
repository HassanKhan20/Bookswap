package com.team1.bookswap.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val password: String,
    val role: String = "buyer"
)

@Entity(tableName = "textbooks")
data class Textbook(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sellerId: Int,
    val title: String,
    val author: String,
    val course: String,
    val price: Double,
    val condition: String,
    val description: String = "",
    @ColumnInfo(defaultValue = "1") val available: Boolean = true
)

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val textbookId: Int,
    val priceAtTime: Double,
    val quantity: Int = 1
)

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val buyerId: Int,
    val total: Double,
    val itemCount: Int,
    val status: String = "confirmed",
    val createdAt: Long = System.currentTimeMillis()
)

data class CartRow(
    val cartId: Int,
    val textbookId: Int,
    val title: String,
    val author: String,
    val priceAtTime: Double,
    val quantity: Int
)
