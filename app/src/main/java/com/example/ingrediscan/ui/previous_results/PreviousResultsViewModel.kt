package com.example.ingrediscan.ui.previous_results

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PreviousResultsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Previous Results Fragment"
    }
    val text: LiveData<String> = _text
}