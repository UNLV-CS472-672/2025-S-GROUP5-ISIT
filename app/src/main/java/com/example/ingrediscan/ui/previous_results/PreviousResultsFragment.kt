package com.example.ingrediscan.ui.previous_results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.ingrediscan.databinding.FragmentPreviousResultsBinding

class PreviousResultsFragment : Fragment() {
    private var _binding: FragmentPreviousResultsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PreviousResultsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviousResultsBinding.inflate(inflater, container, false)

        binding.previousResultsComposeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                PreviousResultsScreen(viewModel) // Removed AppTheme wrapper
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}