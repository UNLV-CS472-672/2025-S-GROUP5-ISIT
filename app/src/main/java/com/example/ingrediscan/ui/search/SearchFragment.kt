package com.example.ingrediscan.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.SearchView
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ingrediscan.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val searchViewModel =
            ViewModelProvider(this).get(SearchViewModel::class.java)

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val searchTitleTextView: TextView = binding.searchTitle

        searchTitleTextView.text = "Search"

        return root
    }
    
    override fun onViewCreated(
        view: View, 
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the SearchView
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            
            // Call the function when user submits a search
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { performSearch(it) } 
                return true
            }

            // Future TODO: filter results live as the user types (set to false right now)
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    // Future TODO: Implement search logic 
    private fun performSearch(query: String) {
        Log.d("SearchFragment", "Searching for: $query")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
