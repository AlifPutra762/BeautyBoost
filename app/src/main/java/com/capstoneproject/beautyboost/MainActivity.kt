package com.capstoneproject.beautyboost

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import com.capstoneproject.beautyboost.data.KEY_ONBOARDING_COMPLETED
import com.capstoneproject.beautyboost.data.KEY_PROFILE_COMPLETE
import com.capstoneproject.beautyboost.data.PREFS_NAME
import com.capstoneproject.beautyboost.databinding.ActivityMainBinding
import com.capstoneproject.beautyboost.ui.GettingStarted1
import com.capstoneproject.beautyboost.ui.HomeActivity
import com.capstoneproject.beautyboost.ui.LoginActivity
import com.capstoneproject.beautyboost.ui.ProfileActivity
import com.capstoneproject.beautyboost.ui.SignInActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val onboardingCompleted = sharedPreferences.getBoolean(KEY_ONBOARDING_COMPLETED, false)
        val onProfileCompleted = sharedPreferences.getBoolean(KEY_PROFILE_COMPLETE, false)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val firebaseUser = auth.currentUser

        if (firebaseUser != null) {
            // Not signed in, launch the Login activity
            if (onProfileCompleted){
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
                return
            } else{
                startActivity(Intent(this, ProfileActivity::class.java))
                finish()
                return
            }

        }else{
            if (!onboardingCompleted && !onProfileCompleted) {

                startActivity(Intent(this, GettingStarted1::class.java))
                finish()
                return

            } else if (onboardingCompleted && !onProfileCompleted) {
                // Start the OnboardingActivity
                startActivity(Intent(this, ProfileActivity::class.java))
                finish()
                return
            }
             else{
                startActivity(Intent(this, SignInActivity::class.java))
                finish()
                return
            }
//            startActivity(Intent(this, GettingStarted1::class.java))
//            finish()
//            return

        }
//
//        val btnLogout = findViewById<Button>(R.id.buttonLogout)
//        btnLogout.setOnClickListener{
//            signOut()
        }
//    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val inflater = menuInflater
//        inflater.inflate(R.menu.main_menu, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.sign_out_menu -> {
//                signOut()
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

//    private fun signOut() {
//        auth.signOut()
//
//        mGoogleSignInClient.signOut().addOnCompleteListener(this) {
//            // Optional: Update UI or show a message to the user
//            val intent = Intent(this@MainActivity, SignInActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//    }
}
