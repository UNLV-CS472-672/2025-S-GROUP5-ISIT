package com.example.ingrediscan.utils

import android.app.AlertDialog
import android.view.Gravity
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import kotlin.math.pow
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

data class CalorieGoals(
    val maintain: Int,
    val mildLoss: Int,
    val loss: Int,
    val extremeLoss: Int
)

// Reusable number picker. Used for Weight and Age
fun Fragment.showNumberPickerDialog(
    title: String,
    minValue: Int,  // Minimum value for the picker
    maxValue: Int,  // Maximum value for the picker
    onValueEntered: (Int) -> Unit
) {
    val layout = LinearLayout(requireContext()).apply {
        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER
        setPadding(50, 20, 50, 20)
    }

    val numberPicker = NumberPicker(requireContext()).apply {
        this.minValue = minValue
        this.maxValue = maxValue
        wrapSelectorWheel = false
    }

    layout.addView(numberPicker)

    val dialog = AlertDialog.Builder(requireContext())
        .setTitle(title)
        .setView(layout)
        .setPositiveButton("OK") { _, _ ->
            onValueEntered(numberPicker.value) // Pass selected value
        }
        .setNegativeButton("Cancel", null)
        .create()

    dialog.show()

    // Ensure dialog is centered and well-sized
    dialog.window?.setLayout(
        (requireContext().resources.displayMetrics.widthPixels * 0.75).toInt(),
        WindowManager.LayoutParams.WRAP_CONTENT
    )
    dialog.window?.setGravity(Gravity.CENTER)
}

// Function for "Height" button - Returns Two Integers (Feet & Inches)
fun Fragment.showHeightPickerDialog(
    onHeightSelected: (feet: Int, inches: Int) -> Unit
) {
    val layout = LinearLayout(requireContext()).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER
    }

    val feetPicker = NumberPicker(requireContext()).apply {
        minValue = 3
        maxValue = 7  // Feet range: 3' to 7'
        wrapSelectorWheel = false
    }

    val inchesPicker = NumberPicker(requireContext()).apply {
        minValue = 0
        maxValue = 11 // Inches range: 0 to 11
        wrapSelectorWheel = false
    }

    layout.addView(feetPicker)
    layout.addView(inchesPicker)

    AlertDialog.Builder(requireContext())
        .setTitle("Select Height")
        .setView(layout)
        .setPositiveButton("OK") { _, _ ->
            onHeightSelected(feetPicker.value, inchesPicker.value)
        }
        .setNegativeButton("Cancel", null)
        .show()
}

// Reusable choice dialog, used in Sex and Activity
fun Fragment.showChoiceDialog(
    title: String,
    choices: List<String>,
    onChoiceSelected: (String) -> Unit
) {
    val choicesArray = choices.toTypedArray() // Convert List to Array for AlertDialog

    AlertDialog.Builder(requireContext())
        .setTitle(title)
        .setItems(choicesArray) { _, which ->
            onChoiceSelected(choicesArray[which]) // Return selected item
        }
        .setNegativeButton("Cancel", null)
        .show()
}

// Calculates BMI from height in feet/inches and weight in pounds
fun calculateBMI(feet: Int, inches: Int, weight: Int): Double {
    val totalInches = feet * 12 + inches
    val heightMeters = totalInches * 0.0254
    val weightKg = weight * 0.453592
    return if (heightMeters > 0) {
        (weightKg / heightMeters.pow(2)).let { "%.1f".format(it).toDouble() }
    } else {
        0.0
    }
}

fun calculateCalorieGoals(
    weight: Int,
    heightFeet: Int,
    heightInches: Int,
    age: Int,
    sex: String,
    activityLevel: String
): CalorieGoals {
    val totalInches = heightFeet * 12 + heightInches

    val bmr = when (sex.lowercase()) {
        "male" -> 66 + (6.23 * weight) + (12.7 * totalInches) - (6.8 * age)
        "female" -> 655 + (4.35 * weight) + (4.7 * totalInches) - (4.7 * age)
        else -> 0.0
    }

    val activityMultiplier = when (activityLevel.lowercase()) {
        "sedentary" -> 1.2
        "lightly active" -> 1.375
        "moderately active" -> 1.55
        "active" -> 1.725
        "very active" -> 1.9
        else -> 1.2
    }

    val maintain = (bmr * activityMultiplier).toInt()
    val mildLoss = (maintain - 250).coerceAtLeast(1200)
    val loss = (maintain - 500).coerceAtLeast(1200)
    val extremeLoss = (maintain - 1000).coerceAtLeast(1200)

    return CalorieGoals(maintain, mildLoss, loss, extremeLoss)
}

// Function to set profile picture in profile page
fun Fragment.setupImagePicker(
    imageView: ImageView
): ActivityResultLauncher<Intent> {
    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = result.data?.data
            imageUri?.let { uri ->
                imageView.setImageURI(uri)
            }
        }
    }

    imageView.setOnClickListener {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        launcher.launch(intent)
    }

    return launcher
}