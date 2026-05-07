package com.nammapustaka.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Book::class, Student::class, BorrowHistory::class], version = 7, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "namma_pustaka_database"
                )
                .fallbackToDestructiveMigration()
                .addCallback(DatabaseCallback())
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    val dao = database.bookDao()
                    
                    try {
                        Log.d("AppDatabase", "Initiating explicit 8 Student prepopulation loop")
                        dao.insertStudent(Student(id = 1, name = "Prajnashree", score = 350, booksRead = 7))
                        dao.insertStudent(Student(id = 2, name = "Ananya", score = 200, booksRead = 4))
                        dao.insertStudent(Student(id = 3, name = "Bharath", score = 150, booksRead = 3))
                        dao.insertStudent(Student(id = 4, name = "Lakshmi", score = 100, booksRead = 2))
                        dao.insertStudent(Student(id = 5, name = "Ravi", score = 210, booksRead = 5))
                        dao.insertStudent(Student(id = 6, name = "Kiran", score = 190, booksRead = 3))
                        dao.insertStudent(Student(id = 7, name = "Deepa", score = 120, booksRead = 2))
                        dao.insertStudent(Student(id = 8, name = "Megha", score = 300, booksRead = 6))
                        Log.d("AppDatabase", "Database insertion success: Configured 8 realistic pseudo identities natively.")
                    } catch (e: Exception) { Log.e("AppDatabase", "Constraint setup failed scaling students: ${e.message}") }
                    
                    try {
                        Log.d("AppDatabase", "Initiating massive 15-target Book library prepopulation hooks")
                        dao.insertBook(Book(title = "Ondu Nenapu", author = "Kuvempu", status = "Available", dueDate = null, borrowerId = null, category = "Literature", isbn = "N/A", shelfLocation = "Rack A1"))
                        
                        val currentTime = System.currentTimeMillis()
                        val overdueTime = currentTime - (3L * 24 * 60 * 60 * 1000) // 3 days ago
                        val futureTime = currentTime + (10L * 24 * 60 * 60 * 1000) // 10 days from now
                        
                        dao.insertBook(Book(title = "Malgudi Days", author = "R.K. Narayan", status = "Borrowed", dueDate = futureTime, borrowerId = 1, category = "Fiction", isbn = "9780143427883", shelfLocation = "Rack B2"))
                        dao.insertBook(Book(title = "Basic Science Volume 1", author = "Govt Publ", status = "Borrowed", dueDate = overdueTime, borrowerId = 1, category = "Science", isbn = "97805520", shelfLocation = "Rack C1"))
                        
                        dao.insertBorrowHistory(BorrowHistory(bookId = 2, bookTitle = "Malgudi Days", borrowDate = currentTime - (4L * 24 * 60 * 60 * 1000), studentId = 1))
                        dao.insertBorrowHistory(BorrowHistory(bookId = 3, bookTitle = "Basic Science Volume 1", borrowDate = currentTime - (17L * 24 * 60 * 60 * 1000), studentId = 1))
                        
                        dao.insertBook(Book(title = "A Brief History of Time", author = "Stephen Hawking", status = "Available", dueDate = null, borrowerId = null, category = "Physics", isbn = "9780553380163", shelfLocation = "Rack C3"))
                        dao.insertBook(Book(title = "The Alchemist", author = "Paulo Coelho", status = "Available", dueDate = null, borrowerId = null, category = "Fiction", isbn = "97800611", shelfLocation = "Rack A2"))
                        dao.insertBook(Book(title = "Ignited Minds", author = "A.P.J. Abdul Kalam", status = "Available", dueDate = null, borrowerId = null, category = "Non-Fiction", isbn = "97801434", shelfLocation = "Rack A3"))
                        dao.insertBook(Book(title = "Harry Potter and the Sorcerer's Stone", author = "J.K. Rowling", status = "Available", dueDate = null, borrowerId = null, category = "Fantasy", isbn = "97805903", shelfLocation = "Rack B1"))
                        dao.insertBook(Book(title = "Indian Polity", author = "M. Laxmikanth", status = "Available", dueDate = null, borrowerId = null, category = "Civics", isbn = "97800706", shelfLocation = "Rack D1"))
                        dao.insertBook(Book(title = "Karvalo", author = "K.P. Poornachandra Tejaswi", status = "Available", dueDate = null, borrowerId = null, category = "Literature", isbn = "N/A", shelfLocation = "Rack A4"))
                        dao.insertBook(Book(title = "Mathematics Grade 10", author = "NCERT", status = "Available", dueDate = null, borrowerId = null, category = "Education", isbn = "12345", shelfLocation = "Rack C2"))
                        
                        dao.insertBook(Book(title = "Srimad Bhagavad Gita", author = "Vyasa", status = "Available", dueDate = null, borrowerId = null, category = "Religion", isbn = "11111", shelfLocation = "Rack D2"))
                        dao.insertBook(Book(title = "Thinking, Fast and Slow", author = "Daniel Kahneman", status = "Available", dueDate = null, borrowerId = null, category = "Psychology", isbn = "22222", shelfLocation = "Rack D3"))
                        dao.insertBook(Book(title = "The Discovery of India", author = "Jawaharlal Nehru", status = "Available", dueDate = null, borrowerId = null, category = "History", isbn = "33333", shelfLocation = "Rack B3"))
                        dao.insertBook(Book(title = "Wings of Fire", author = "A.P.J. Abdul Kalam", status = "Available", dueDate = null, borrowerId = null, category = "Autobiography", isbn = "44444", shelfLocation = "Rack C4"))
                        dao.insertBook(Book(title = "Kusuma Bale", author = "K.P. Poornachandra Tejaswi", status = "Available", dueDate = null, borrowerId = null, category = "Literature", isbn = "N/A", shelfLocation = "Rack A5"))
                        
                        Log.d("AppDatabase", "Database insertion success: Configured all 15 targeted global resources correctly")
                    } catch (e: Exception) { Log.e("AppDatabase", "Insertion failure during library initialization: ${e.message}") }
                }
            }
        }
    }
}
