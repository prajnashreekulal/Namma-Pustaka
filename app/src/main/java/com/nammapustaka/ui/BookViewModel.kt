package com.nammapustaka.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nammapustaka.data.Book
import com.nammapustaka.data.BookDao
import com.nammapustaka.data.BorrowHistory
import com.nammapustaka.data.Student
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class BookViewModel(private val bookDao: BookDao) : ViewModel() {

    // Role State Constraints
    private val _userRole = MutableStateFlow("student")
    val userRole = _userRole.asStateFlow()

    fun toggleRole() {
        _userRole.value = if (_userRole.value == "student") "admin" else "student"
    }

    // Search Pipeline
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    val searchResults: StateFlow<List<Book>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                bookDao.getAllBooks()
            } else {
                bookDao.searchBooksByTitle(query)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val borrowedBooks: StateFlow<List<Book>> = bookDao.getBorrowedBooks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val leaderboard: StateFlow<List<Student>> = bookDao.getLeaderboard()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalUsers: StateFlow<Int> = bookDao.getTotalUsersCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val currentStudent: StateFlow<Student?> = bookDao.getCurrentStudent()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val currentStudentRank: StateFlow<Int> = combine(leaderboard, currentStudent) { board, me ->
        if (me == null) return@combine 0
        board.indexOfFirst { it.id == me.id } + 1
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val borrowHistory: StateFlow<List<BorrowHistory>> = currentStudent
        .flatMapLatest { student ->
            if (student != null) {
                bookDao.getBorrowHistoryForUser(student.id)
            } else {
                kotlinx.coroutines.flow.flowOf(emptyList())
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBorrowHistory: StateFlow<List<BorrowHistory>> = bookDao.getAllBorrowHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBooks: StateFlow<List<Book>> = bookDao.getAllBooks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun insertBook(book: Book) {
        viewModelScope.launch {
            bookDao.insertBook(book)
        }
    }

    suspend fun tryBorrowBook(bookId: Int, bookTitle: String): Boolean {
        val book = bookDao.getBookById(bookId)
        if (book != null && book.status == "Available") {
            val student = currentStudent.value
            val currentStudentId = student?.id ?: 1 
            val dueDateMs = System.currentTimeMillis() + (14L * 24 * 60 * 60 * 1000) // Updated to 14 Days explicitly
            
            Log.d("BookViewModel", "Passing checkout boundaries for item ID ${bookId} under user ${currentStudentId}")
            bookDao.updateStatusAndBorrower(bookId, "Borrowed", currentStudentId, dueDateMs)
            
            bookDao.insertBorrowHistory(BorrowHistory(bookId = bookId, bookTitle = book.title, borrowDate = System.currentTimeMillis(), studentId = currentStudentId))
            return true
        }
        Log.e("BookViewModel", "Transaction blocked: Item ID $bookId invalid or already checked-out")
        return false 
    }

    fun returnBook(bookId: Int) {
        viewModelScope.launch {
            Log.d("BookViewModel", "Executing return protocols mapping ID $bookId")
            bookDao.returnBookCheckout(bookId)
            val student = currentStudent.value
            if (student != null) {
                bookDao.addScoreToStudent(student.id, 50) 
            }
        }
    }
}

class BookViewModelFactory(private val bookDao: BookDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookViewModel(bookDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
