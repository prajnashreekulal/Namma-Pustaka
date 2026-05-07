package com.nammapustaka.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.nammapustaka.ui.BookViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
@Composable
fun ScanScreen(viewModel: BookViewModel, onBorrowSuccess: () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()
    val userRole by viewModel.userRole.collectAsState()

    var hasCameraPermission by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
    }
    
    var scannedValue by remember { mutableStateOf<String?>(null) }
    var feedbackState by remember { mutableStateOf(0) } 

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    LaunchedEffect(key1 = true) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    if (userRole == "admin") {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                "Admins cannot borrow books via QR. Please switch to Student role.",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold
            )
        }
        return
    }

    if (!hasCameraPermission) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Camera permission required")
        }
        return
    }

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    val options = BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build()
    val scanner = BarcodeScanning.getClient(options)

    val imageAnalysis = remember {
        ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also {
                it.setAnalyzer(cameraExecutor) { imageProxy ->
                    val mediaImage = imageProxy.image
                    if (mediaImage != null && scannedValue == null) {
                        val image = com.google.mlkit.vision.common.InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                        scanner.process(image)
                            .addOnSuccessListener { barcodes ->
                                for (barcode in barcodes) {
                                    barcode.rawValue?.let { value ->
                                        scannedValue = value
                                        scope.launch {
                                            val id = value.toIntOrNull()
                                            if (id != null) {
                                                val success = viewModel.tryBorrowBook(id, "QR Checkout")
                                                if (success) {
                                                    feedbackState = 1 
                                                    delay(1500)
                                                    onBorrowSuccess()
                                                } else {
                                                    feedbackState = 2 
                                                }
                                            } else {
                                                feedbackState = 2
                                            }
                                        }
                                    }
                                }
                            }
                            .addOnCompleteListener { imageProxy.close() }
                    } else {
                        imageProxy.close()
                    }
                }
            }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                androidx.camera.view.PreviewView(ctx).apply {
                    this.scaleType = androidx.camera.view.PreviewView.ScaleType.FILL_CENTER
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(this.surfaceProvider)
                        }
                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageAnalysis)
                        } catch (exc: Exception) {
                            exc.printStackTrace()
                        }
                    }, ContextCompat.getMainExecutor(ctx))
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        if (scannedValue != null) {
            Surface(
                modifier = Modifier.align(Alignment.Center).padding(32.dp),
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f),
                shadowElevation = 8.dp
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    if (feedbackState == 1) {
                       Icon(Icons.Filled.Check, contentDescription = "Success", tint = Color(0xFF4CAF50), modifier = Modifier.size(48.dp))
                       Spacer(modifier = Modifier.height(8.dp))
                       Text("Book Borrowed Successfully", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                       Text("Valid for 14 days. ID: $scannedValue", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    } else if (feedbackState == 2) {
                       Icon(Icons.Filled.Warning, contentDescription = "Error", tint = Color.Red, modifier = Modifier.size(48.dp))
                       Spacer(modifier = Modifier.height(8.dp))
                       Text("Checkout Failed", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                       Text("Book is already Borrowed or Not Found!", color = MaterialTheme.colorScheme.error)
                       Spacer(modifier = Modifier.height(16.dp))
                       Button(onClick = { scannedValue = null; feedbackState = 0 }) {
                           Text("Scan Again")
                       }
                    } else {
                       CircularProgressIndicator()
                       Spacer(modifier = Modifier.height(8.dp))
                       Text("Verifying Library Database...", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            val infiniteTransition = rememberInfiniteTransition()
            val scanLineOffset by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 250f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1500, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )

            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                val rectSize = 250.dp.toPx()
                val left = (canvasWidth - rectSize) / 2
                val top = (canvasHeight - rectSize) / 2
                val right = left + rectSize
                val bottom = top + rectSize
                val overlayColor = Color.Black.copy(alpha = 0.6f)
                
                drawRect(color = overlayColor, topLeft = Offset.Zero, size = Size(canvasWidth, top))
                drawRect(color = overlayColor, topLeft = Offset(0f, bottom), size = Size(canvasWidth, canvasHeight - bottom))
                drawRect(color = overlayColor, topLeft = Offset(0f, top), size = Size(left, rectSize))
                drawRect(color = overlayColor, topLeft = Offset(right, top), size = Size(canvasWidth - right, rectSize))
                
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(left, top),
                    size = Size(rectSize, rectSize),
                    cornerRadius = CornerRadius(24.dp.toPx()),
                    style = Stroke(width = 4.dp.toPx())
                )
                
                drawLine(
                    color = Color(0xFF4CAF50),
                    start = Offset(left + 16.dp.toPx(), top + scanLineOffset.dp.toPx()),
                    end = Offset(right - 16.dp.toPx(), top + scanLineOffset.dp.toPx()),
                    strokeWidth = 3.dp.toPx()
                )
            }
            
            Text(
                text = "Align QR inside the frame",
                modifier = Modifier.align(Alignment.Center).offset(y = 160.dp).background(Color.Black.copy(alpha=0.4f), RoundedCornerShape(8.dp)).padding(horizontal = 16.dp, vertical = 8.dp),
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
