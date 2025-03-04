package com.example.ingrediscan.BackEnd.TextRecognition

import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.tasks.await

/**
 * Helper object to perform text recognition using ML Kit.
 */
object TextRecognitionHelper {
    /**
     * Extracts text from a given bitmap image.
     * @param bitmap The image to process
     * @param rotation The image rotation the picture is at to allow for dynamic adjustment in degrees
     */
    suspend fun extractTextFromImage(bitmap: Bitmap, rotation: Int): String {
        return try {
            val image = InputImage.fromBitmap(bitmap, rotation)
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            val visionText = recognizer.process(image).await() // Uses Kotlin coroutines
            visionText.text
        } catch (e: Exception) {
            Log.e("OCR", "Text extraction failed", e)
            throw e
        }
    }
}
