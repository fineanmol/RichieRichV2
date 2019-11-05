package com.nightowldevelopers.richierich.ui.notifications

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class NotificationsViewModel : ViewModel() {


    private val _text = MutableLiveData<String>().apply {

        value = "This is Profile Fragment"
    }
    val text: LiveData<String> = _text
}