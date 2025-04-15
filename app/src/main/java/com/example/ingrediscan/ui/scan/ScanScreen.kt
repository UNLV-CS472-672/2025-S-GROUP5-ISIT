package com.example.ingrediscan.ui.scan

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ingrediscan.R
import com.example.ingrediscan.ui.theme.lightGreen
// Compose
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner

// CameraX
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
// AndroidX Utils
import androidx.core.content.ContextCompat

@Composable
fun CameraPreview() {
    // Get the current context and lifecycle owner from the Compose environment
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Use AndroidView to embed a traditional Android View (PreviewView) into a Compose UI
    AndroidView(
        factory = { ctx ->
            // PreviewView is a CameraX component that displays the camera feed
            val previewView = PreviewView(ctx)

            // Asynchronously get a camera provider instance
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

            // When the camera provider is ready, configure the preview use case
            cameraProviderFuture.addListener({
                // Get the actual CameraProvider
                val cameraProvider = cameraProviderFuture.get()

                // Build the camera preview use case
                val preview = Preview.Builder().build().also {
                    // Set the PreviewView as the surface provider for the preview
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                // Select the back-facing camera
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                // Unbind all previous use cases (required before re-binding)
                cameraProvider.unbindAll()

                // Bind the preview use case to the lifecycle of the screen
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,    // Makes preview stop/start with the screen
                    cameraSelector,    // Specifies the back camera
                    preview            // The preview use case to display camera feed
                )
            }, ContextCompat.getMainExecutor(ctx)) // Run on the main UI thread

            // Return the native Android view that will be embedded in Compose
            previewView
        },
        modifier = Modifier.fillMaxSize() // Make the camera preview fill the entire screen
    )
}


@Composable
fun ScanScreen(
    viewModel: ScanViewModel = viewModel(),
    onNavigateHome: () -> Unit
) {
    BoxWithCutout(viewModel, onNavigateHome)
}

@Composable
fun BoxWithCutout(
    viewModel: ScanViewModel,
    onNavigateHome: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                compositingStrategy = CompositingStrategy.Offscreen
            }
            .drawWithCache {
                onDrawWithContent {
                    // Draw the main content
                    drawContent()

                    // Define the size and position of the rounded rectangle
                    val rectWidth = 350.dp.toPx() // Width of the rounded rectangle
                    val rectHeight = 600.dp.toPx() // Height of the rounded rectangle
                    val cornerRadius = 20.dp.toPx() // Corner radius of the rounded rectangle

                    // Draw a rounded rectangle with BlendMode.Clear to create the cutout
                    drawRoundRect(
                        color = Color.Black,
                        topLeft = Offset(
                            (size.width - rectWidth) / 2, // Center the rectangle horizontally
                            (size.height - rectHeight) / 2 - 50.dp.toPx() // Move up by 70dp
                        ),
                        size = Size(rectWidth, rectHeight),
                        cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                        blendMode = BlendMode.Clear // Use BlendMode.Clear to cut out the rectangle
                    )
                }
            }
    ) {
        // Show camera as main background
        CameraPreview()
        // Semi-transparent black overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.80f))
        ) {
            // Your content goes here
        }

        // ButtonRow at the bottom of the screen
        ButtonRow(
            modifier = Modifier
                .align(Alignment.BottomCenter) // Align to the bottom center
                .padding(bottom = 16.dp), // Add some padding at the bottom
            onImageButtonClick = { viewModel.onImageButtonClick() }
        )

        // Example of observing LiveData and using the toggleFlash function
        val isFlashOn by viewModel.isFlashOn.observeAsState(false)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp, 20.dp, 15.dp, 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Arrow Icon",
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        viewModel.onArrowClick(onNavigateHome)
                    },
                tint = Color.White
            )
            Image(
                painter = painterResource(id = R.drawable.boltauto), // Flash Icon
                contentDescription = "Change flash on/off", // Accessibility description
                modifier = Modifier
                    .size(35.dp)
                    .clickable { viewModel.toggleFlash() },
                colorFilter = ColorFilter.tint(Color.White)
            )
            Icon(
                imageVector = Icons.Default.Settings, // Settings Icon
                contentDescription = "Settings", // Accessibility description
                modifier = Modifier
                    .size(40.dp)
                    .clickable { viewModel.onSettingsClick() },
                tint = Color.White
            )
        }
    }
}

@Composable
fun ButtonRow(modifier: Modifier = Modifier, onImageButtonClick:() -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp) // Add horizontal padding
            .padding(bottom = 30.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {},
//            modifier = Modifier
//                .size(80.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = lightGreen,
                contentColor = Color.White
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.applepng), // Replace with your drawable resource
                contentDescription = "Produce Icon", // Accessibility description
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .size(50.dp) // Set the size of the image
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        // Image button (replaces the Box)
        ImageButton(
            onClick = onImageButtonClick, // Pass the click handler
            imageRes = R.drawable.aperture, // Replace with your drawable resource
            contentDescription = "Image Button", // Accessibility description
            modifier = Modifier
                .size(100.dp), // Set the size of the image button
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {},
//            modifier = Modifier
//                .size(80.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = lightGreen,
                contentColor = Color.White
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.barpng), // Replace with your drawable resource
                contentDescription = "Barcode Icon", // Accessibility description
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .size(50.dp) // Set the size of the image
            )
        }
    }
}

// The composable for the camera button to scan item/code
@Composable
fun ImageButton(
    onClick: () -> Unit,
    imageRes: Int, // Drawable resource ID for the image
    contentDescription: String, // Accessibility description
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable(onClick = onClick) // Make the image clickable
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(), // Fill the Box with the image
            colorFilter = ColorFilter.tint(Color.White)
        )
    }
}
