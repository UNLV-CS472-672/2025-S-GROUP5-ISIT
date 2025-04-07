package com.example.ingrediscan.ui.previous_results

data class ScannedItem(
    val name: String,
    val brand: String,
    val calories: Int,      // in kcal
    val protein: Int,       // in grams
    val carbs: Int,         // in grams
    val fat: Int,           // in grams
    val grade: String,
)