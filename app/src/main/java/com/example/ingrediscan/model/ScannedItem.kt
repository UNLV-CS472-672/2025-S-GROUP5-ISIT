package com.example.ingrediscan.model

import android.graphics.Bitmap

/**
 * Represents a user's scanned item, and they will be stored in a list in user class.
 *
 * @property image the image of the scanned item.
 * @property itemID Unique identifier (primary key) for the user's list of scanned item data.
 * @property itemName the name of the scanned item.
 * @property calories the calories count of the scanned item.
 *
 */
data class ScannedItem (
    val image: Bitmap,
    val itemID: Int,
    val itemName: String,
    val calories: Int
)