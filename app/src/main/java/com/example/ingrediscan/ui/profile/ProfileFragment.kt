package com.example.ingrediscan.ui.profile
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ingrediscan.databinding.FragmentProfileBinding
import com.example.ingrediscan.utils.*

private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        // Use image picker utility (allow user to set profile picture)
        pickImageLauncher = setupImagePicker(binding.profileButtonImage)

        // Show profile banner
        binding.profileBanner.visibility = View.VISIBLE

        // Buttons
        val weightButton = binding.profileButtonWeight
        val heightButton = binding.profileButtonHeight
        val ageButton = binding.profileButtonAge
        val sexButton = binding.profileButtonSex
        val activityLevelButton = binding.profileButtonActivityLevel

        // *** Weight Button ***
        profileViewModel.weight.observe(viewLifecycleOwner) { newWeight ->
            weightButton.text = "Weight: $newWeight lbs"
        }
        weightButton.setOnClickListener {
            showNumberPickerDialog("Enter Weight", 50, 500) { weight ->
                profileViewModel.setWeight(weight)
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
            showNumberPickerDialog("Select Age", 13, 110) { age ->
                profileViewModel.setAge(age)
                profileViewModel.updateCalorieGoals()
            }
        }

        // *** Sex Button ***
        profileViewModel.sex.observe(viewLifecycleOwner) { newSex ->
            sexButton.text = "Sex: $newSex"
        }
        sexButton.setOnClickListener {
            showChoiceDialog("Select Sex", listOf("Male", "Female")) { selectedSex ->
                profileViewModel.setSex(selectedSex)
                profileViewModel.updateCalorieGoals()
            }
        }

        // *** Activity Level Button ***
        profileViewModel.activityLevel.observe(viewLifecycleOwner) { newActivityLevel ->
            activityLevelButton.text = "Activity Level: $newActivityLevel"
        }
        activityLevelButton.setOnClickListener {
            val levels = listOf("Sedentary", "Lightly Active", "Moderately Active", "Active", "Very Active")
            showChoiceDialog("Select Activity Level", levels) { selected ->
                profileViewModel.setActivityLevel(selected)
                profileViewModel.updateCalorieGoals()
            }
        }

        // *** BMI Text ***
        profileViewModel.bmi.observe(viewLifecycleOwner) { bmi ->
            binding.bmiText.text = "BMI: $bmi"
        }

        // *** Calorie Suggestions ***
        profileViewModel.calorieGoals.observe(viewLifecycleOwner) { goals ->
            val weight = profileViewModel.weight.value
            val feet = profileViewModel.heightFeet.value
            val inches = profileViewModel.heightInches.value
            val age = profileViewModel.age.value
            val sex = profileViewModel.sex.value
            val activity = profileViewModel.activityLevel.value

            val missing = mutableListOf<String>()
            if (weight == null || weight == 0) missing.add("weight")
            if (feet == null || feet == 0 || inches == null) missing.add("height")
            if (age == null || age == 0) missing.add("age")
            if (sex == null || sex == "N/A") missing.add("sex")
            if (activity == null || activity == "N/A") missing.add("activity level")

            binding.caloricSuggestionTextView.text = if (missing.isNotEmpty()) {
                "Please enter ${missing.joinToString(", ")}"
            } else {
                """
                    Maintain: ${goals.maintain} kcal
                    Mild Loss: ${goals.mildLoss} kcal
                    Loss: ${goals.loss} kcal
                    Extreme Loss: ${goals.extremeLoss} kcal
                """.trimIndent()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
