package com.nightowldevelopers.richierich.ui.notifications

import android.content.pm.CrossProfileApps
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.nightowldevelopers.richierich.R
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //var Name = arguments?.getString("Name")


//        var Name1 = arguments!!.getString("Name")
        notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        val username: TextView = root.findViewById(R.id.name_text)
        val useremail: TextView = root.findViewById(R.id.email_text)
        val userphoto: ImageView = root.findViewById(R.id.user_photo)
        notificationsViewModel.text.observe(this, Observer {
          //  if(Name1!=null)
            username.text = currentUser?.displayName
            useremail.text=currentUser?.email
            Picasso.get().load(currentUser?.photoUrl).into(userphoto)
        })


        return root


    }
}
