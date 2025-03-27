package com.example.ingrediscan.BackEnd.TextRecognition

import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.tasks.await

/**
 * Helper object to perform text recognition, ingredient parsing, and barcode scanning.
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
            val visionText = recognizer.process(image).await()
            visionText.text
        } catch (e: Exception) {
            Log.e("OCR", "Text extraction failed", e)
            throw e
        }
    }

    /**
     * Parses OCR text to extract individual ingredient names.
     * @param ocrText The raw text from OCR processing
     * @return List of cleaned ingredient names
     */
    fun extractIngredientsFromText(ocrText: String): List<String> {
        return try {
            val patternsToRemove = listOf(
                "INGREDIENTS:", "INGREDIENTS", "CONTAINS:", "CONTAINS",
                "\\d+%",        // Percentages
                "\\*",          // Asterisks
                "\\d+",         // Standalone numbers
                "\\(.*?\\)",    // Parentheses content
                "\\*?Nutrient"  // Remove "Nutrient" with or without an asterisk
            )

            var processedText = ocrText
                .replace("\n", ", ") // Replace newlines with commas
                .replace(";", ",")   // Standardize delimiters

            // Remove common patterns
            patternsToRemove.forEach { pattern ->
                processedText = processedText.replace(Regex(pattern, RegexOption.IGNORE_CASE), "")
            }

            processedText.split(",")
                .map { it.trim() } // Ensure no leading/trailing spaces
                .filter { it.isNotBlank() }
                .filterNot { it.equals("and", ignoreCase = true) } // Ignore "and" as a standalone item
                .map { it.replace(Regex("\\.$"), "") } // Remove trailing periods
                .map { it.replace(Regex("\\s+"), " ") } // Normalize spaces
                .map { it.trim() } // Final trim to avoid trailing spaces
                .distinct()
                .sorted()
        } catch (e: Exception) {
            Log.e("IngredientParser", "Failed to parse ingredients", e)
            emptyList()
        }
    }

    /**
     * Scans for barcodes in a given bitmap image.
     * @param bitmap The image to process
     * @param rotation The image rotation (in degrees)
     * @return List of barcode values (empty if none found)
     */
    suspend fun scanBarcodes(bitmap: Bitmap, rotation: Int): List<String> {
        return try {
            val image = InputImage.fromBitmap(bitmap, rotation)
            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                    Barcode.FORMAT_UPC_A,
                    Barcode.FORMAT_UPC_E,
                    Barcode.FORMAT_EAN_8,
                    Barcode.FORMAT_EAN_13,
                    Barcode.FORMAT_CODE_128,
                    Barcode.FORMAT_CODE_39,
                    Barcode.FORMAT_QR_CODE
                )
                .build()

            val scanner = BarcodeScanning.getClient(options)
            val barcodes = scanner.process(image).await()

            barcodes.mapNotNull { barcode ->
                barcode.rawValue?.trim()
            }.distinct() // Remove duplicates
        } catch (e: Exception) {
            Log.e("BarcodeScanner", "Barcode scanning failed", e)
            emptyList()
        }
    }

    /**
     * Combined function to extract both ingredients and barcodes from an image.
     * @param bitmap The image to process
     * @param rotation The image rotation (in degrees)
     * @return Pair containing list of ingredients and list of barcode values
     */
    suspend fun extractProductInfo(bitmap: Bitmap, rotation: Int): Pair<List<String>, List<String>> {
        val ingredients = try {
            val text = extractTextFromImage(bitmap, rotation)
            extractIngredientsFromText(text)
        } catch (e: Exception) {
            emptyList()
        }

        val barcodes = try {
            scanBarcodes(bitmap, rotation)
        } catch (e: Exception) {
            emptyList()
        }

        return Pair(ingredients, barcodes)
    }
}