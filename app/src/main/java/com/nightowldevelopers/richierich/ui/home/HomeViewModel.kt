package com.nightowldevelopers.richierich.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "What's your Richness Rank\n Donate more to improve your rank!!"
    }
    val text: LiveData<String> = _text
}