package com.example.ingrediscan.BackEnd.TextRecognition

import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

/**
 * Helper object to perform text recognition using ML Kit.
 */
object TextRecognitionHelper {
    /**
     * Interface to handle the text recognition result callbacks.
     */
    interface TextRecognitionCallback {
        fun onTextExtracted(text: String) // Called when text is found
        fun onError(e: Exception) // Called upon failure
    }

    /**
     * Extracts text from a given bitmap image.
     * @param bitmap The image to process
     * @param callback Callback to return the extracted text or an error
     */
    fun extractTextFromImage(bitmap: Bitmap, callback: TextRecognitionCallback) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        // Process the image using ML Kit
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                callback.onTextExtracted(visionText.text)
            }
            .addOnFailureListener { e ->
                callback.onError(e)
                Log.e("OCR", "Text extraction failed", e)
            }
    }
}
