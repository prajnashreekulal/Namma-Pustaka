package com.nammapustaka.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import kotlinx.coroutines.delay
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import java.io.File
import java.io.FileOutputStream
import com.nammapustaka.data.Book
import com.nammapustaka.ui.BookViewModel

@Composable
fun AddBookScreen(viewModel: BookViewModel) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var shelfLocation by remember { mutableStateOf("") }
    var isbn by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<String?>(null) }
    
    var showSuccess by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            val file = File(context.cacheDir, "book_cover_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            imageUri = Uri.fromFile(file).toString()
        }
    }

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
            .padding(bottom = 80.dp)
    ) {
        Text("Add New Book", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Book Title") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = author,
            onValueChange = { author = it },
            label = { Text("Author Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Category") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = shelfLocation,
            onValueChange = { shelfLocation = it },
            label = { Text("Shelf Location (Optional)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = isbn,
            onValueChange = { isbn = it },
            label = { Text("ISBN (Optional)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { cameraLauncher.launch(null) },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text("Capture Book Cover")
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = "Cover Preview",
                modifier = Modifier.fillMaxWidth().height(150.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = {
                if (title.isNotBlank() && author.isNotBlank() && category.isNotBlank()) {
                    val newBook = Book(
                        title = title,
                        author = author,
                        category = category,
                        status = "Available",
                        dueDate = null,
                        borrowerId = null,
                        isbn = isbn.ifBlank { "N/A" },
                        shelfLocation = shelfLocation.ifBlank { "Unassigned" },
                        imageUri = imageUri
                    )
                    viewModel.insertBook(newBook)
                    title = ""
                    author = ""
                    category = ""
                    shelfLocation = ""
                    isbn = ""
                    imageUri = null
                    showSuccess = true
                    focusManager.clearFocus()
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Save Book")
        }

        if (showSuccess) {
            LaunchedEffect(showSuccess) {
                delay(2000)
                showSuccess = false
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Book Added Successfully!", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        }
    }
}
