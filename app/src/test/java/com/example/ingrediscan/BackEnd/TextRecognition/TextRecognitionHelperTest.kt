package com.example.ingrediscan.BackEnd.TextRecognition


import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.O_MR1])
class TextRecognitionHelperTest {

    @Test
    fun extractTextFromImage() = runBlocking {
        try {
            TextRecognitionHelper.extractTextFromImage(createTestBitmap(), 0)
            fail("Expected exception to be thrown")
        } catch (e: Exception) {
            // Expected exception - parameter intentionally unused
        }
    }

    @Test
    fun extractIngredientsFromText() {
        // Test simple ingredient list
        val simpleInput = "INGREDIENTS: water, sugar, salt, natural flavors"
        val simpleExpected = listOf("natural flavors", "salt", "sugar", "water")
        assertEquals(simpleExpected, TextRecognitionHelper.extractIngredientsFromText(simpleInput))

        // Test complex formatting
        val complexInput = """
            INGREDIENTS: Wheat Flour* (contains Calcium Carbonate,
            Iron, Niacin, Thiamine), Water, Salt (1.5%), Yeast,
            Vegetable Oil, Soy Flour, Emulsifier (E472e),
            Flour Treatment Agent (Ascorbic Acid*).
            *Nutrient
        """.trimIndent()
        val complexExpected = listOf(
            "Emulsifier", "Flour Treatment Agent", "Salt",
            "Soy Flour", "Vegetable Oil", "Water",
            "Wheat Flour", "Yeast"
        )
        assertEquals(complexExpected, TextRecognitionHelper.extractIngredientsFromText(complexInput))

        // Test empty input
        assertTrue(TextRecognitionHelper.extractIngredientsFromText("").isEmpty())

        // Test with various delimiters
        val delimitersInput = "flour; water. salt\n yeast, and milk"
        val delimitersExpected = listOf("flour", "milk", "salt", "water", "yeast")
        assertEquals(delimitersExpected, TextRecognitionHelper.extractIngredientsFromText(delimitersInput))
    }

    @Test
    fun scanBarcodes() = runBlocking {
        try {
            TextRecognitionHelper.scanBarcodes(createTestBitmap(), 0)
            fail("Expected exception to be thrown")
        } catch (e: Exception) {
            // Expected exception - parameter intentionally unused
        }
    }

    @Test
    fun extractProductInfo() = runBlocking {
        try {
            TextRecognitionHelper.extractProductInfo(createTestBitmap(), 0)
            fail("Expected exception to be thrown")
        } catch (e: Exception) {
            // Expected exception - parameter intentionally unused
        }
    }

    private fun createTestBitmap(): Bitmap {
        return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888).apply {
            setPixel(0, 0, Color.WHITE)
        }
    }
}