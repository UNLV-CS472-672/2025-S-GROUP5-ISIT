package com.example.ingrediscan.ui.scan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ingrediscan.databinding.FragmentScanBinding
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.example.ingrediscan.R


class ScanFragment : Fragment() {

    private lateinit var scanViewModel: ScanViewModel
    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        val root: View = binding.root
        scanViewModel = ViewModelProvider(this)[ScanViewModel::class.java]

        binding.scanComposeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ScanScreen(
                    viewModel = scanViewModel,
                    onNavigateHome = {
                        findNavController().navigate(R.id.navigation_home)
                    }
                )
            }
        }
        return root
    }

    override fun onResume() {
        super.onResume()
        hideSystemBars()
    }

    override fun onPause() {
        super.onPause()
        showSystemBars()
    }

    // Hides the system UI (status bar + navigation bar) for fullscreen mode
    private fun hideSystemBars() {
        // Get the window from the activity (if it exists)
        activity?.window?.let { window ->
            // Allow your content to draw behind system bars
            WindowCompat.setDecorFitsSystemWindows(window, false)

            // Create a controller to interact with system bars
            val controller = WindowInsetsControllerCompat(window, window.decorView)
            // Hide both the status bar (top) and nav bar (bottom or gesture area)
            controller.hide(
                WindowInsetsCompat.Type.statusBars() or
                        WindowInsetsCompat.Type.navigationBars()
            )

            // Allow the user to swipe to briefly show them (like in camera apps)
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    // Shows the system UI again (brings back status + nav bars)
    private fun showSystemBars() {
        activity?.window?.let { window ->
            // Go back to letting system bars take up layout space
            WindowCompat.setDecorFitsSystemWindows(window, true)

            // Create the same controller to interact with system bars
            val controller = WindowInsetsControllerCompat(window, window.decorView)

            // Show status and nav bars again
            controller.show(
                WindowInsetsCompat.Type.statusBars() or
                        WindowInsetsCompat.Type.navigationBars()
            )
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
