package com.nammapustaka.ui.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import coil.compose.SubcomposeAsyncImage
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.nammapustaka.data.Book
import com.nammapustaka.ui.BookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShelfScreen(viewModel: BookViewModel) {
    val books by viewModel.searchResults.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    var selectedBook by remember { mutableStateOf<Book?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Namma Pustaka", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text("Smart Rural Library", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f))
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.updateSearchQuery(it) },
            placeholder = { Text("Search by title or author...", style = MaterialTheme.typography.bodyLarge) },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.primary) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(50),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            ),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        if (books.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No books found matching criteria.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 80.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(books) { book ->
                    BookCardAesthetic(book = book, onClick = {
                        selectedBook = book
                        showBottomSheet = true
                    })
                }
            }
        }
    }

    if (showBottomSheet && selectedBook != null) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(24.dp).fillMaxWidth().padding(bottom = 32.dp)) {
                Text(selectedBook?.title ?: "", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(selectedBook?.author ?: "", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("Category", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Text(selectedBook?.category ?: "N/A")
                    }
                    Column {
                        Text("Shelf Location", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Text(selectedBook?.shelfLocation ?: "N/A")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Text("Status", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Text(selectedBook?.status ?: "Unknown", color = if (selectedBook?.status == "Available") Color(0xFF4CAF50) else Color.Red, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Column {
                    Text("Summary", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(selectedBook?.summary ?: "No summary available.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    }
}

@Composable
fun BookCardAesthetic(book: Book, onClick: () -> Unit) {
    val currentDate = System.currentTimeMillis()
    val isOverdue = book.dueDate != null && currentDate > book.dueDate && book.status == "Borrowed"
    val displayStatus = if (isOverdue) "OVERDUE" else book.status
    
    val statusColor = if (isOverdue) Color(0xFFD32F2F) else if (book.status == "Borrowed") Color(0xFFE57373) else Color(0xFF81C784)

    val (gradientColors, fallbackIcon) = when (book.category?.trim()?.lowercase()) {
        "science" -> listOf(Color(0xFF2196F3), Color(0xFF00BCD4)) to androidx.compose.material.icons.Icons.Filled.Info
        "history" -> listOf(Color(0xFFFF9800), Color(0xFFFFC107)) to androidx.compose.material.icons.Icons.Filled.Place
        "fiction" -> listOf(Color(0xFF9C27B0), Color(0xFFE91E63)) to androidx.compose.material.icons.Icons.Filled.Face
        "biography" -> listOf(Color(0xFF4CAF50), Color(0xFF8BC34A)) to androidx.compose.material.icons.Icons.Filled.Person
        "self help" -> listOf(Color(0xFF3F51B5), Color(0xFF03A9F4)) to androidx.compose.material.icons.Icons.Filled.ThumbUp
        "technology" -> listOf(Color(0xFF607D8B), Color(0xFF455A64)) to androidx.compose.material.icons.Icons.Filled.Build
        "education" -> listOf(Color(0xFF009688), Color(0xFF00796B)) to androidx.compose.material.icons.Icons.Filled.List
        "philosophy" -> listOf(Color(0xFF795548), Color(0xFF5D4037)) to androidx.compose.material.icons.Icons.Filled.AccountBox
        "literature" -> listOf(Color(0xFF673AB7), Color(0xFF512DA8)) to androidx.compose.material.icons.Icons.Filled.Home
        "kannada" -> listOf(Color(0xFFF44336), Color(0xFFFF5722)) to androidx.compose.material.icons.Icons.Filled.Star
        "novel" -> listOf(Color(0xFFE91E63), Color(0xFFC2185B)) to androidx.compose.material.icons.Icons.Filled.Favorite
        "programming" -> listOf(Color(0xFF00BCD4), Color(0xFF0097A7)) to androidx.compose.material.icons.Icons.Filled.Settings
        "general knowledge" -> listOf(Color(0xFFFFC107), Color(0xFFFFA000)) to androidx.compose.material.icons.Icons.Filled.Search
        else -> listOf(Color(0xFF424242), Color(0xFF212121)) to androidx.compose.material.icons.Icons.Filled.Info
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            if (!book.imageUri.isNullOrBlank()) {
                SubcomposeAsyncImage(
                    model = book.imageUri,
                    contentDescription = "Cover",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop,
                    error = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Brush.linearGradient(gradientColors)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(fallbackIcon, contentDescription = "Cover", modifier = Modifier.size(48.dp), tint = Color.White.copy(alpha = 0.8f))
                        }
                    }
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Brush.linearGradient(gradientColors)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(fallbackIcon, contentDescription = "Cover", modifier = Modifier.size(48.dp), tint = Color.White.copy(alpha = 0.8f))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = book.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(text = book.author, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f), maxLines = 1)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = book.shelfLocation ?: "Rack Info N/A", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f), maxLines = 1)
            Spacer(modifier = Modifier.height(12.dp))
            
            Surface(
                color = statusColor.copy(alpha = 0.15f),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = displayStatus,
                    color = statusColor,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
