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

@Database(entities = [Book::class, Student::class, BorrowHistory::class], version = 8, exportSchema = false)
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
                        Log.d("AppDatabase", "Initiating massive 20-target Book library prepopulation hooks with summaries")
                        val currentTime = System.currentTimeMillis()
                        val overdueTime = currentTime - (3L * 24 * 60 * 60 * 1000)
                        val futureTime = currentTime + (10L * 24 * 60 * 60 * 1000)

                        dao.insertBook(Book(title = "Ondu Nenapu", author = "Kuvempu", status = "Available", category = "Literature", isbn = "N/A", shelfLocation = "Rack A1", summary = "A profound collection of memories and reflections by the Rashtrakavi Kuvempu. Exploring rural life, nature, and the human condition in the heart of Karnataka."))
                        dao.insertBook(Book(title = "Malgudi Days", author = "R.K. Narayan", status = "Borrowed", dueDate = futureTime, borrowerId = 1, category = "Fiction", isbn = "9780143427883", shelfLocation = "Rack B2", summary = "A timeless collection of short stories set in the fictional South Indian town of Malgudi, capturing the essence of Indian society with humor and grace."))
                        dao.insertBook(Book(title = "Basic Science Volume 1", author = "Govt Publ", status = "Borrowed", dueDate = overdueTime, borrowerId = 1, category = "Science", isbn = "97805520", shelfLocation = "Rack C1", summary = "An introductory textbook to foundational scientific concepts including physics, chemistry, and biology tailored for high school students."))
                        
                        dao.insertBorrowHistory(BorrowHistory(bookId = 2, bookTitle = "Malgudi Days", borrowDate = currentTime - (4L * 24 * 60 * 60 * 1000), studentId = 1))
                        dao.insertBorrowHistory(BorrowHistory(bookId = 3, bookTitle = "Basic Science Volume 1", borrowDate = currentTime - (17L * 24 * 60 * 60 * 1000), studentId = 1))
                        
                        dao.insertBook(Book(title = "A Brief History of Time", author = "Stephen Hawking", status = "Available", category = "Science", isbn = "9780553380163", shelfLocation = "Rack C3", summary = "A landmark volume in science writing by one of the great minds of our time, explaining complex cosmological ideas in clear, accessible language."))
                        dao.insertBook(Book(title = "The Alchemist", author = "Paulo Coelho", status = "Available", category = "Fiction", isbn = "97800611", shelfLocation = "Rack A2", summary = "A magical story about an Andalusian shepherd boy named Santiago who travels from his homeland in Spain to the Egyptian desert in search of a treasure buried in the Pyramids."))
                        dao.insertBook(Book(title = "Ignited Minds", author = "A.P.J. Abdul Kalam", status = "Available", category = "Non-Fiction", isbn = "97801434", shelfLocation = "Rack A3", summary = "A compelling book where Dr. Kalam urges the youth of India to dream big and realize their inner potential to build a developed nation."))
                        dao.insertBook(Book(title = "Harry Potter and the Sorcerer's Stone", author = "J.K. Rowling", status = "Available", category = "Fiction", isbn = "97805903", shelfLocation = "Rack B1", summary = "An orphaned boy discovers he is a wizard and attends a magical school where he uncovers the truth about his past and his destiny."))
                        dao.insertBook(Book(title = "Indian Polity", author = "M. Laxmikanth", status = "Available", category = "Education", isbn = "97800706", shelfLocation = "Rack D1", summary = "The ultimate comprehensive guide to the Indian political system and constitution, essential for competitive exam preparation."))
                        dao.insertBook(Book(title = "Karvalo", author = "K.P. Poornachandra Tejaswi", status = "Available", category = "Literature", isbn = "N/A", shelfLocation = "Rack A4", summary = "A masterpiece of Kannada literature that follows a scientist and local villagers into the dense forests of the Western Ghats searching for a rare flying lizard."))
                        dao.insertBook(Book(title = "Mathematics Grade 10", author = "NCERT", status = "Available", category = "Education", isbn = "12345", shelfLocation = "Rack C2", summary = "The official NCERT textbook covering core mathematical principles like trigonometry, geometry, and algebra for 10th-grade students."))
                        dao.insertBook(Book(title = "Srimad Bhagavad Gita", author = "Vyasa", status = "Available", category = "Philosophy", isbn = "11111", shelfLocation = "Rack D2", summary = "An ancient Indian text providing profound philosophical wisdom on duty, righteousness, and the path to spiritual liberation."))
                        dao.insertBook(Book(title = "Thinking, Fast and Slow", author = "Daniel Kahneman", status = "Available", category = "Psychology", isbn = "22222", shelfLocation = "Rack D3", summary = "A groundbreaking tour of the mind explaining the two systems that drive the way we think: the fast, intuitive system, and the slow, deliberate system."))
                        dao.insertBook(Book(title = "The Discovery of India", author = "Jawaharlal Nehru", status = "Available", category = "History", isbn = "33333", shelfLocation = "Rack B3", summary = "Written during his imprisonment, Nehru offers a broad panorama of Indian history, culture, and philosophy tracing its roots from ancient times."))
                        dao.insertBook(Book(title = "Wings of Fire", author = "A.P.J. Abdul Kalam", status = "Available", category = "Biography", isbn = "44444", shelfLocation = "Rack C4", summary = "The inspiring autobiography of the 'Missile Man of India', detailing his journey from a humble background to becoming the President of India."))
                        dao.insertBook(Book(title = "Kusuma Bale", author = "Devenuru Mahadeva", status = "Available", category = "Literature", isbn = "N/A", shelfLocation = "Rack A5", summary = "A deeply moving Kannada novel exploring caste, rural dynamics, and human relationships with poetic brilliance."))
                        dao.insertBook(Book(title = "Clean Code", author = "Robert C. Martin", status = "Available", category = "Technology", isbn = "97801323", shelfLocation = "Rack E1", summary = "A classic handbook of agile software craftsmanship teaching developers how to write clean, readable, and maintainable code."))
                        dao.insertBook(Book(title = "The Pragmatic Programmer", author = "Andrew Hunt", status = "Available", category = "Technology", isbn = "97802016", shelfLocation = "Rack E2", summary = "A must-read for any software developer, packed with practical advice and best practices for modern programming challenges."))
                        dao.insertBook(Book(title = "Sapiens", author = "Yuval Noah Harari", status = "Available", category = "History", isbn = "97800623", shelfLocation = "Rack B4", summary = "A sweeping narrative exploring the history of the human species, from the Stone Age to the modern era, examining how biology and history define us."))
                        dao.insertBook(Book(title = "Atomic Habits", author = "James Clear", status = "Available", category = "Self Help", isbn = "97807352", shelfLocation = "Rack D4", summary = "An incredibly practical guide providing a proven framework for improving every day by making tiny changes that lead to remarkable results."))
                        dao.insertBook(Book(title = "Steve Jobs", author = "Walter Isaacson", status = "Available", category = "Biography", isbn = "97814516", shelfLocation = "Rack C5", summary = "The exclusive biography of the Apple co-founder, based on more than forty interviews with Jobs conducted over two years."))
                        dao.insertBook(Book(title = "Data Structures and Algorithms", author = "Narasimha Karumanchi", status = "Available", category = "Programming", isbn = "97881932", shelfLocation = "Rack E3", summary = "A comprehensive textbook detailing fundamental computer science algorithms, data structures, and problem-solving techniques."))
                        
                        Log.d("AppDatabase", "Database insertion success: Configured all 20 targeted global resources correctly")
                    } catch (e: Exception) { Log.e("AppDatabase", "Insertion failure during library initialization: ${e.message}") }
                }
            }
        }
    }
}
