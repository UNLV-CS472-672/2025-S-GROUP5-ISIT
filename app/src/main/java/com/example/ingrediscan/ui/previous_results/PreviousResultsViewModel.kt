package com.example.ingrediscan.ui.previous_results

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PreviousResultsViewModel : ViewModel() {
    private val _scannedItems = MutableLiveData<List<ScannedItem>>()
    val scannedItems: LiveData<List<ScannedItem>> = _scannedItems

    init {
        loadMockData() // Replace with your actual data source
    }

    private fun loadMockData() {
        viewModelScope.launch {
            _scannedItems.value = listOf( // Right now just test items
                ScannedItem("Wild Blueberry Granola Bars", "Sister Fruit Company", 146, 8, 7, 12, "B+"),
                ScannedItem("Organic Peanut Butter", "Healthy Farms", 210, 9, 5, 18, "A"),
                ScannedItem("Protein Shake", "Muscle Fuel", 150, 25, 6, 2, "C-"),
                ScannedItem("Greek Yogurt", "YogurtLand", 100, 10, 7, 3, "D"),
                ScannedItem("Dark Chocolate", "Cocoa Bliss", 170, 2, 12, 14, "C+"),
                ScannedItem("Almond Milk", "Nutty Farms", 60, 1, 8, 3, "F"),
                ScannedItem("Oatmeal Cookies", "Grandma's Bakes", 180, 3, 22, 9, "D-")
            )
        }
    }

    fun onAccountClick() { /* Handle profile click */ } // Later implementation
}
