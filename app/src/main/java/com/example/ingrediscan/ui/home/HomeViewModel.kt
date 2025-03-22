
package com.example.ingrediscan.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _caloriesTracked = MutableLiveData<Int>(800)
    val caloriesTracked: LiveData<Int> = _caloriesTracked

    private val _progress = MutableLiveData<Int>(90)
    val progress: LiveData<Int> = _progress

    private val _label = MutableLiveData<String>("Calories Tracked Today")
    val label: LiveData<String> = _label

    fun updateCalories(calories: Int) {
        _caloriesTracked.value = calories
        _progress.value = calculateProgress(calories)
    }

    private fun calculateProgress(calories: Int): Int {
        // Example calculation: progress based on a 2000-calorie goal
        return (calories / 2000.0 * 100).toInt()
    }
}



