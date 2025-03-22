package com.example.ingrediscan.ui.previous_results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ingrediscan.databinding.FragmentPreviousResultsBinding

class PreviousResultsFragment : Fragment() {

    private var _binding: FragmentPreviousResultsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(PreviousResultsViewModel::class.java)

        _binding = FragmentPreviousResultsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textPreviousResults
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}