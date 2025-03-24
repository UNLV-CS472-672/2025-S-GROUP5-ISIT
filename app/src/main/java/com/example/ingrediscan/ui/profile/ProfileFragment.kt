package com.example.ingrediscan.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ingrediscan.databinding.FragmentProfileBinding
import com.example.ingrediscan.utils.showChoiceDialog

// Utilities.kt imports
import com.example.ingrediscan.utils.showNumberPickerDialog
import com.example.ingrediscan.utils.showHeightPickerDialog

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Buttons
        val profileBanner = binding.profileBanner
        val weightButton = binding.profileButtonWeight
        val heightButton = binding.profileButtonHeight
        val ageButton = binding.profileButtonAge
        val sexButton = binding.profileButtonSex
        val activityLevelButton = binding.profileButtonActivityLevel

        profileBanner.visibility = View.VISIBLE

        // *** Weight Button ***
        profileViewModel.weight.observe(viewLifecycleOwner) { newWeight ->
            weightButton.text = "Weight: $newWeight lbs"
        }
        weightButton.setOnClickListener {
            showNumberPickerDialog("Enter Weight", 50, 500) { enteredWeight ->
                profileViewModel.setWeight(enteredWeight)
                profileViewModel.updateBMI()
                profileViewModel.updateCalorieGoals()
            }
        }

        // *** Height Button ***
        profileViewModel.heightFeet.observe(viewLifecycleOwner) { feet ->
            profileViewModel.heightInches.observe(viewLifecycleOwner) { inches ->
                heightButton.text = "Height: ${feet}'${inches}\""
            }
        }
        heightButton.setOnClickListener {
            showHeightPickerDialog { feet, inches ->
                profileViewModel.setHeight(feet, inches)
                profileViewModel.updateBMI()
                profileViewModel.updateCalorieGoals()
            }
        }

        // *** Age Button ***
        profileViewModel.age.observe(viewLifecycleOwner) { newAge ->
            ageButton.text = "Age: $newAge years"
        }
        ageButton.setOnClickListener {
            showNumberPickerDialog("Select Age", 13, 110) { selectedAge ->
                profileViewModel.setAge(selectedAge)
                profileViewModel.updateCalorieGoals()
            }
        }

        // *** Sex Button ***
        profileViewModel.sex.observe(viewLifecycleOwner) { newSex ->
            sexButton.text = "Sex: $newSex"
        }
        sexButton.setOnClickListener {
            val sexOptions = listOf("Male", "Female")
            showChoiceDialog("Select Sex", sexOptions) { selectedSex ->
                profileViewModel.setSex(selectedSex)
                profileViewModel.updateCalorieGoals()
            }
        }
        // *** Activity Level Button ***
        profileViewModel.activityLevel.observe(viewLifecycleOwner) { newActivityLevel ->
            activityLevelButton.text = "Activity Level: $newActivityLevel"
        }
        activityLevelButton.setOnClickListener {
            val activityOptions = listOf("Sedentary", "Lightly Active", "Moderately Active", "Active", "Very Active")
            showChoiceDialog("Select Activity Level", activityOptions) { selectedActivity ->
                profileViewModel.setActivityLevel(selectedActivity)
                profileViewModel.updateCalorieGoals()
            }
        }

        // Observe BMI and update text
        profileViewModel.bmi.observe(viewLifecycleOwner) { bmi ->
            binding.bmiText.text = "BMI: $bmi"
        }

        // Update calorie suggestions
        profileViewModel.calorieGoals.observe(viewLifecycleOwner) { goals ->
            val weight = profileViewModel.weight.value
            val feet = profileViewModel.heightFeet.value
            val inches = profileViewModel.heightInches.value
            val age = profileViewModel.age.value
            val sex = profileViewModel.sex.value
            val activity = profileViewModel.activityLevel.value

            val missingInputs = mutableListOf<String>()

            if (weight == null || weight == 0) missingInputs.add("weight")
            if (feet == null || feet == 0 || inches == null) missingInputs.add("height")
            if (age == null || age == 0) missingInputs.add("age")
            if (sex == null || sex == "N/A") missingInputs.add("sex")
            if (activity == null || activity == "N/A") missingInputs.add("activity level")

            if (missingInputs.isNotEmpty()) {
                val message = "Please enter ${missingInputs.joinToString(", ")}"
                binding.caloricSuggestionTextView.text = message
            } else {
                val text = """
            Maintain: ${goals.maintain} kcal
            Mild Loss: ${goals.mildLoss} kcal
            Loss: ${goals.loss} kcal
            Extreme Loss: ${goals.extremeLoss} kcal
        """.trimIndent()
                binding.caloricSuggestionTextView.text = text
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}