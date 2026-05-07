package com.nammapustaka.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM books ORDER BY id DESC")
    fun getAllBooks(): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE status = 'Borrowed'")
    fun getBorrowedBooks(): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE title LIKE '%' || :query || '%' ORDER BY title ASC")
    fun searchBooksByTitle(query: String): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE id = :bookId LIMIT 1")
    suspend fun getBookById(bookId: Int): Book?

    @Insert
    suspend fun insertBook(book: Book)

    @Update
    suspend fun updateBook(book: Book)

    @Query("UPDATE books SET status = :status, borrowerId = :borrowerId, dueDate = :dueDate WHERE id = :bookId")
    suspend fun updateStatusAndBorrower(bookId: Int, status: String, borrowerId: Int?, dueDate: Long?)

    @Query("UPDATE books SET status = 'Available', borrowerId = NULL, dueDate = NULL WHERE id = :bookId")
    suspend fun returnBookCheckout(bookId: Int)

    // Gamification & Ranking Parameters
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStudent(student: Student)

    @Query("SELECT * FROM students ORDER BY score DESC, booksRead DESC")
    fun getLeaderboard(): Flow<List<Student>>

    @Query("SELECT * FROM students LIMIT 1")
    fun getCurrentStudent(): Flow<Student?>

    @Query("UPDATE students SET booksRead = booksRead + 1, score = score + :points WHERE id = :studentId")
    suspend fun addScoreToStudent(studentId: Int, points: Int)

    @Query("SELECT COUNT(*) FROM students")
    fun getTotalUsersCount(): Flow<Int>

    // Enhanced Profile History
    @Insert
    suspend fun insertBorrowHistory(history: BorrowHistory)

    @Query("SELECT * FROM borrow_history WHERE studentId = :studentId ORDER BY borrowDate DESC")
    fun getBorrowHistoryForUser(studentId: Int): Flow<List<BorrowHistory>>

    @Query("SELECT * FROM borrow_history ORDER BY borrowDate DESC")
    fun getAllBorrowHistory(): Flow<List<BorrowHistory>>
}
