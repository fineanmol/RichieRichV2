package com.nightowldevelopers.richierich.ui.home

import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.nightowldevelopers.richierich.R

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    val userref = FirebaseDatabase.getInstance().getReference("donate")
    lateinit var ref: DatabaseReference
    val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val button1: Button = root.findViewById(R.id.button)
        val button2: Button = root.findViewById(R.id.button2)
        val button3: Button = root.findViewById(R.id.button3)
        val button4: Button = root.findViewById(R.id.button4)
        val button5: Button = root.findViewById(R.id.button_5)
        val button6: Button = root.findViewById(R.id.button6)
        val button7: Button = root.findViewById(R.id.button7)




        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(this, Observer {
            textView.text = it
        })


        //Button Click method

        return root
    }
}