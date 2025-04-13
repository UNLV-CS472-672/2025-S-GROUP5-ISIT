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
            .background(Color.Gray) // Main background (white or any other content)
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
