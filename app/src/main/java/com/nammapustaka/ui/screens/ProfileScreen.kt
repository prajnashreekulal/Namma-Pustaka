package com.nammapustaka.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nammapustaka.ui.BookViewModel

@Composable
fun ProfileScreen(viewModel: BookViewModel) {
    val userRole by viewModel.userRole.collectAsState()

    if (userRole == "admin") {
        val allHistory by viewModel.allBorrowHistory.collectAsState()
        val allBooks by viewModel.allBooks.collectAsState()
        val leaderboard by viewModel.leaderboard.collectAsState()

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("Library Manager", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(24.dp))
            
            Text("Borrow Monitor", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            if (allHistory.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No borrow records found.", color = Color.Gray, style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(allHistory) { history ->
                        val book = allBooks.find { it.id == history.bookId }
                        val student = leaderboard.find { it.id == history.studentId }
                        
                        val isCurrentlyBorrowedByThisStudent = book?.status == "Borrowed" && book?.borrowerId == history.studentId
                        
                        val statusText: String
                        val statusColor: Color
                        
                        if (isCurrentlyBorrowedByThisStudent && book != null) {
                            val isOverdue = book.dueDate != null && System.currentTimeMillis() > book.dueDate
                            if (isOverdue) {
                                statusText = "OVERDUE"
                                statusColor = Color(0xFFD32F2F)
                            } else {
                                statusText = "Borrowed"
                                statusColor = Color(0xFFE57373)
                            }
                        } else {
                            statusText = "Returned"
                            statusColor = Color(0xFF81C784)
                        }

                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Text(history.bookTitle, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, maxLines = 1, modifier = Modifier.weight(1f))
                                    Surface(
                                        color = statusColor.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(50)
                                    ) {
                                        Text(
                                            text = statusText,
                                            color = statusColor,
                                            style = MaterialTheme.typography.labelSmall,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Student: ${student?.name ?: "Unknown"}", style = MaterialTheme.typography.bodyMedium)
                                Text("Borrowed: ${java.util.Date(history.borrowDate).toString().substring(0, 10)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f))
                                if (isCurrentlyBorrowedByThisStudent && book?.dueDate != null) {
                                    Text("Due: ${java.util.Date(book.dueDate).toString().substring(0, 10)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f))
                                }
                            }
                        }
                    }
                }
            }
        }
        return
    }

    val currentStudent by viewModel.currentStudent.collectAsState()
    val borrowedBooks by viewModel.borrowedBooks.collectAsState()
    val borrowHistory by viewModel.borrowHistory.collectAsState()

    val currentStudentId = currentStudent?.id
    val myBorrowedBooks = borrowedBooks.filter { it.borrowerId == currentStudentId } 
    val overdueBooks = myBorrowedBooks.filter { it.dueDate != null && System.currentTimeMillis() > it.dueDate }
    val filteredHistory = borrowHistory.filter { it.studentId == currentStudentId }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        val title = currentStudent?.name?.let { "$it's Profile" } ?: "My Profile"
        Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))

        if (overdueBooks.isNotEmpty()) {
            Surface(
                color = MaterialTheme.colorScheme.errorContainer,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Warning, contentDescription = "Warning", tint = MaterialTheme.colorScheme.onErrorContainer)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "You have ${overdueBooks.size} OVERDUE books! Please return them immediately.",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        Text("Currently Borrowed", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        if (myBorrowedBooks.isEmpty()) {
            Text("No books borrowed right now.", modifier = Modifier.padding(8.dp), color = Color.Gray)
        } else {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(myBorrowedBooks) { book ->
                    ElevatedCard(
                        modifier = Modifier.width(200.dp).height(130.dp), 
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text(book.title, fontWeight = FontWeight.Bold, maxLines = 1, style = MaterialTheme.typography.titleMedium)
                                val daysLeft = book.dueDate?.let { ((it - System.currentTimeMillis()) / 86400000).toInt() } ?: 0
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(if (daysLeft < 0) "Overdue by ${-daysLeft} days" else "$daysLeft days left", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = if (daysLeft < 0) Color(0xFFD32F2F) else MaterialTheme.colorScheme.primary)
                            }
                            Button(
                                onClick = { viewModel.returnBook(book.id) },
                                modifier = Modifier.fillMaxWidth().height(36.dp),
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text("Return Book", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text("Reading History", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        if (filteredHistory.isEmpty()) {
            Text("Your reading history is empty.", modifier = Modifier.padding(8.dp), color = Color.Gray)
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(filteredHistory) { history ->
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(), 
                        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text(history.bookTitle, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSecondaryContainer)
                                Text("Borrowed: ${java.util.Date(history.borrowDate).toString().substring(0, 10)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f))
                            }
                        }
                    }
                }
            }
        }
    }
}
