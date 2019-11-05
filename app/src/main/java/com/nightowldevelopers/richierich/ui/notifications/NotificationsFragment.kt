package com.nightowldevelopers.richierich.ui.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.nightowldevelopers.richierich.R

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //var Name = arguments?.getString("Name")
        try {
            Log.d("Argument Value",arguments.toString())
            Log.d("Name",arguments?.getString("Name"))
        } catch (e: Exception) {
            Log.d("Error",e.message)
        }

//        var Name1 = arguments!!.getString("Name")
        notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        notificationsViewModel.text.observe(this, Observer {
          //  if(Name1!=null)
            textView.text = it
        })


        return root
    }
}
