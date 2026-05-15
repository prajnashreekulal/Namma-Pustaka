package com.nammapustaka.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val author: String,
    val status: String, // e.g., "Available", "Borrowed"
    val dueDate: Long? = null, // nullable now
    val borrowerId: Int? = null, // new relation field
    val category: String,
    val coverImageUrl: String? = null,
    val isbn: String? = null,
    val shelfLocation: String? = null,
    val imageUri: String? = null,
    val summary: String = "No summary available."
)
