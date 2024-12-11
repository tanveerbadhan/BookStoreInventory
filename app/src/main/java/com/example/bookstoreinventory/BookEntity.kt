package com.example.bookstoreinventory

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey
    val bookId: String,
    val author: String,
    val title: String,
    val price: Double,
    val quantity:Int,
)
