package com.example.ingrediscan.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ingrediscan.utils.CalorieGoals
import com.example.ingrediscan.utils.calculateBMI
import com.example.ingrediscan.utils.calculateCalorieGoals


class ProfileViewModel : ViewModel() {

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

    private val _calorieGoals = MutableLiveData<CalorieGoals>()
    val calorieGoals: LiveData<CalorieGoals> = _calorieGoals

    private val _bmi = MutableLiveData(0.0)
    val bmi: LiveData<Double> = _bmi



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

    fun updateBMI() {
        val feet = _heightFeet.value
        val inches = _heightInches.value
        val weightVal = _weight.value

        if (feet != null && inches != null && weightVal != null) {
            _bmi.value = calculateBMI(feet, inches, weightVal)
        }
    }

    fun updateCalorieGoals() {
        val feet = _heightFeet.value
        val inches = _heightInches.value
        val weightVal = _weight.value
        val ageVal = _age.value
        val sexVal = _sex.value
        val activityVal = _activityLevel.value

        if (feet != null && inches != null && weightVal != null && ageVal != null && sexVal != null && activityVal != null) {
            _calorieGoals.value = calculateCalorieGoals(
                weightVal, feet, inches, ageVal, sexVal, activityVal
            )
        }
    }
}