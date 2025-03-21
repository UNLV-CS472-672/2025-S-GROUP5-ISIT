package com.example.ingrediscan.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ingrediscan.R
import kotlin.math.pow

class ProfileViewModel : ViewModel() {
    private val _profileBanner = MutableLiveData<Int>(R.drawable.test_logo)
    val profileBanner: LiveData<Int> = _profileBanner

    private val _weight = MutableLiveData(0) // Default value
    val weight: LiveData<Int> = _weight

    private val _heightFeet = MutableLiveData(0) // Default value
    val heightFeet: LiveData<Int> = _heightFeet

    private val _heightInches = MutableLiveData(0) // Default value
    val heightInches: LiveData<Int> = _heightInches

    private val _age = MutableLiveData(0) // Default age
    val age: LiveData<Int> = _age

    private val _sex = MutableLiveData("N/A") // Default sex
    val sex: LiveData<String> = _sex

    private val _activityLevel = MutableLiveData("N/A") // Default activity level
    val activityLevel: LiveData<String> = _activityLevel

    private val _bmi = MutableLiveData(0.0)
    val bmi: LiveData<Double> = _bmi

    private val _idealWeightRange = MutableLiveData("N/A")
    val idealWeightRange: LiveData<String> = _idealWeightRange

    // Update weight
    fun setWeight(value: Int) {
        _weight.value = value
    }

    // Update height
    fun setHeight(feet: Int, inches: Int) {
        _heightFeet.value = feet
        _heightInches.value = inches
    }

    // Update age
    fun setAge(value: Int) {
        _age.value = value
    }

    // Update sex
    fun setSex(value: String){
        _sex.value = value
    }

    // Update activity level
    fun setActivityLevel(value: String){
        _activityLevel.value = value
    }

    private fun calculateBMIAndIdealWeight() {
        val weightKg = _weight.value ?: return
        val feet = _heightFeet.value ?: return
        val inches = _heightInches.value ?: return

        // Convert height to meters
        val heightMeters = ((feet * 12) + inches) * 0.0254

        // Calculate BMI
        val bmiValue = weightKg / heightMeters.pow(2)
        _bmi.value = String.format("%.2f", bmiValue).toDouble()

        // Calculate Ideal Weight Range (BMI 18.5 to 24.9)
        val minWeight = (18.5 * heightMeters.pow(2)).toInt()
        val maxWeight = (24.9 * heightMeters.pow(2)).toInt()
        _idealWeightRange.value = "$minWeight kg - $maxWeight kg"
    }
}