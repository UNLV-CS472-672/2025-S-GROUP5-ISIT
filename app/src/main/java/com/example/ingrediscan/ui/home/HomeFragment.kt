package com.example.ingrediscan.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ingrediscan.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val caloriesTextView: TextView = binding.caloriesTracked
        val labelTextView: TextView = binding.caloriesLabel
        val progressBar: ProgressBar = binding.progressBar
        val appNameTextView: TextView = binding.appName
        val taglineTextView: TextView = binding.tagline

        homeViewModel.caloriesTracked.observe(viewLifecycleOwner) {
            caloriesTextView.text = it.toString()
        }

        homeViewModel.label.observe(viewLifecycleOwner) {
            labelTextView.text = it
        }

        homeViewModel.progress.observe(viewLifecycleOwner){
            progressBar.progress = it
        }
        appNameTextView.text = "IngrediScan"
        taglineTextView.text = "Nutrition transparency at your fingertips"


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
