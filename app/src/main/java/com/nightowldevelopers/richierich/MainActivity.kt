package com.nightowldevelopers.richierich

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import com.nightowldevelopers.richierich.ui.home.HomeViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {
    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]
    private lateinit var database: DatabaseReference


    private lateinit var googleSignInClient: GoogleSignInClient
    private val currentUser = FirebaseAuth.getInstance().currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        database = FirebaseDatabase.getInstance().reference
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var gbtn = findViewById<Button>(R.id.google_button)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = FirebaseAuth.getInstance()


        if (currentUser != null) {
            database = FirebaseDatabase.getInstance().reference
            currentUser?.let { user ->

                val userNameRef = database.child("users")?.orderByChild("email")?.equalTo(user.email)
                val eventListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) = if (!dataSnapshot.exists()) {
                        //create new user
                        NewUser(
                            currentUser.displayName.toString(),
                            currentUser.email.toString(),
                            currentUser.photoUrl.toString()
                        )

                    } else {
                        //user found!!
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                    }
                }
                userNameRef?.addListenerForSingleValueEvent(eventListener)
            }

        }
        else{
            signIn()
        }


    }


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                // [START_EXCLUDE]
                updateUI(null)
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)
        // [START_EXCLUDE silent]
        showProgressDialog()
        // [END_EXCLUDE]

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                    progressBar.visibility = View.GONE
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    //Snackbar.make(main_layout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                    updateUI(null)
                }

                // [START_EXCLUDE]
                hideProgressDialog()
                // [END_EXCLUDE]
            }
    }
    // [END auth_with_google]

    // [START signin]
    private fun signIn() {
        revokeAccess()
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    // [END signin]

    private fun signOut() {
        // Firebase sign out
        // auth.signOut()

        // Google sign out
        googleSignInClient.signOut().addOnCompleteListener(this) {
            updateUI(null)
        }
    }

    private fun revokeAccess() {
        // Firebase sign out
        //auth.signOut()

        // Google revoke access
        googleSignInClient.revokeAccess().addOnCompleteListener(this) {
            updateUI(null)
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        hideProgressDialog()
        if (user != null) {
            /*Log.d("TAG", "lOGGED IN")
            Log.d("Tag", user.displayName)
            Log.d("Tag", user.photoUrl.toString())
            Log.d("Tag", user.email)*/


            val homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
            val root = layoutInflater.inflate(R.layout.fragment_home, container, false)
            val textView: TextView = root.findViewById(R.id.text_home)
            homeViewModel.text.observe(this, Observer {
                textView.text = user.email.toString()
            })

            progressBar.visibility = View.GONE
        } else {

        }
    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }

    @IgnoreExtraProperties
    data class User(
        var username: String? = "",
        var email: String? = "", var profile: String? = "", var amount: Int? = 0
    )


    private fun NewUser(name: String, email: String?, profile: String?) {
        database = FirebaseDatabase.getInstance().reference
        var amount= 0
        var username= email?.split("@")?.first()
        val user = User(name, email, profile, amount)
        database.child("users").child(username.toString()).setValue(user)
        if (email != null) {
            database.child("users").child(username.toString())
        }
    }


}