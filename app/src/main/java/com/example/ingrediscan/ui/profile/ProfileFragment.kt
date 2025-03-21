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
        val bmiTextView = binding.bmiText
        val idealWeightTextView = binding.idealWeightText

        profileBanner.let{
            it.visibility = View.VISIBLE
        }

        // *** Weight Button ***
        profileViewModel.weight.observe(viewLifecycleOwner) { newWeight ->
            weightButton.text = "Weight: $newWeight lbs"
        }
        weightButton.setOnClickListener {
            showNumberPickerDialog("Enter Weight", 50, 500) { enteredWeight ->
                profileViewModel.setWeight(enteredWeight) // Store as Int
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
                profileViewModel.setHeight(feet, inches) // Store as Int values
            }
        }

        // *** Age Button ***
        profileViewModel.age.observe(viewLifecycleOwner) { newAge ->
            ageButton.text = "Age: $newAge years"
        }
        ageButton.setOnClickListener {
            showNumberPickerDialog("Select Age", 13, 110) { selectedAge ->
                profileViewModel.setAge(selectedAge) // Store as Int
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
            }
        }

        // *** Activity Level Button ***
        profileViewModel.activityLevel.observe(viewLifecycleOwner) { newActivityLevel ->
            activityLevelButton.text = "Activity Level: $newActivityLevel"
        }
        activityLevelButton.setOnClickListener {
            val activityOptions = listOf("Sedentary", "Lightly Active", "Moderately Active",
                "Active", "Very Active")
            showChoiceDialog("Select Activity Level", activityOptions) { selectedActivity ->
                profileViewModel.setActivityLevel(selectedActivity)
            }
        }

        // Observe and update BMI & Ideal Weight
        profileViewModel.bmi.observe(viewLifecycleOwner) { newBMI ->
            bmiTextView.text = "BMI: $newBMI"
        }
        profileViewModel.idealWeightRange.observe(viewLifecycleOwner) { idealWeight ->
            idealWeightTextView.text = "Ideal Weight: $idealWeight"
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}