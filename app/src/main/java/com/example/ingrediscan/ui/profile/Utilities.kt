package com.example.ingrediscan.utils

import android.app.AlertDialog
import android.view.Gravity
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.NumberPicker
import androidx.fragment.app.Fragment

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