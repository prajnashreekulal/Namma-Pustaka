package com.nammapustaka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import com.nammapustaka.ui.BookViewModel
import com.nammapustaka.ui.BookViewModelFactory
import com.nammapustaka.ui.screens.MainScreen

class MainActivity : ComponentActivity() {
    
    private val bookViewModel: BookViewModel by viewModels {
        val application = application as NammaPustakaApplication
        BookViewModelFactory(application.database.bookDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val defaultTypography = Typography()
            val customTypography = Typography(
                displayLarge = defaultTypography.displayLarge.copy(fontFamily = FontFamily.Cursive),
                displayMedium = defaultTypography.displayMedium.copy(fontFamily = FontFamily.Cursive),
                displaySmall = defaultTypography.displaySmall.copy(fontFamily = FontFamily.Cursive),
                headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = FontFamily.Cursive),
                headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = FontFamily.Cursive),
                headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = FontFamily.Cursive),
                titleLarge = defaultTypography.titleLarge.copy(fontFamily = FontFamily.Cursive),
                titleMedium = defaultTypography.titleMedium.copy(fontFamily = FontFamily.SansSerif),
                titleSmall = defaultTypography.titleSmall.copy(fontFamily = FontFamily.SansSerif),
                bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = FontFamily.SansSerif),
                bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = FontFamily.SansSerif),
                bodySmall = defaultTypography.bodySmall.copy(fontFamily = FontFamily.SansSerif),
                labelLarge = defaultTypography.labelLarge.copy(fontFamily = FontFamily.SansSerif),
                labelMedium = defaultTypography.labelMedium.copy(fontFamily = FontFamily.SansSerif),
                labelSmall = defaultTypography.labelSmall.copy(fontFamily = FontFamily.SansSerif)
            )

            MaterialTheme(typography = customTypography) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(viewModel = bookViewModel)
                }
            }
        }
    }
}
