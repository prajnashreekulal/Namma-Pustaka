package com.nammapustaka.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "borrow_history")
data class BorrowHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val bookId: Int,
    val bookTitle: String,
    val borrowDate: Long,
    val studentId: Int // Explicit structural relation binding specific users securely
)
