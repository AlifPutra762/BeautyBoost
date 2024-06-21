package com.capstoneproject.beautyboost.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstoneproject.beautyboost.R
import com.capstoneproject.beautyboost.data.KEY_ONBOARDING_COMPLETED
import com.capstoneproject.beautyboost.data.KEY_PROFILE_COMPLETE
import com.capstoneproject.beautyboost.data.PREFS_NAME
import com.capstoneproject.beautyboost.databinding.ActivityHomeBinding
import com.capstoneproject.beautyboost.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    val auth = Firebase.auth
    val user = auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (user != null) {
            val userName = user.displayName?.split(" ")?.get(0) ?: "User"
            binding.name.text = "Hi, " + userName + "  \uD83D\uDC4B"
        } else {
            // Handle the case when the user is not signed in
        }

        binding.analyzeButton.root.setOnClickListener {
            val intent = Intent(this, ScanActivity::class.java)
            startActivity(intent)
        }

        // Set up RecyclerView
        val articlesAdapter = ArticlesAdapter(getArticlesList())
        binding.articlesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.articlesRecyclerView.adapter = articlesAdapter

        if (user == null) {
            // Not signed in, launch the Login activity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

    }

    private fun getArticlesList(): List<Article> {
        // Dummy data, replace with real data fetching logic
        return listOf(
            Article("Penyebab Jerawat: Faktor Internal dan Eksternal",
                "Artikel ini membahas berbagai penyebab jerawat, termasuk faktor hormonal, kebersihan kulit, dan pengaruh lingkungan."),
            Article("Cara Efektif Mengatasi Jerawat Membandel",
                "Temukan berbagai metode dan produk perawatan kulit yang efektif untuk mengatasi jerawat yang sulit dihilangkan."),
            Article("Mitos dan Fakta Tentang Jerawat",
                "Mengungkap berbagai mitos umum tentang jerawat dan memberikan fakta berdasarkan penelitian ilmiah."),
            Article("Perawatan Alami untuk Kulit Berjerawat",
                "Pelajari tentang bahan-bahan alami yang dapat membantu mengurangi jerawat dan menjaga kesehatan kulit."),
            Article("Peran Diet dalam Kesehatan Kulit: Menghindari dan Mengurangi Jerawat",
                "Artikel ini membahas bagaimana pola makan dapat mempengaruhi kesehatan kulit dan memberikan tips diet untuk mencegah jerawat.")
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sign_out_menu -> {
                signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun signOut() {

        lifecycleScope.launch {
            val credentialManager = CredentialManager.create(this@HomeActivity)
            auth.signOut()

            val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

            with(sharedPreferences.edit()) {
                putBoolean(KEY_ONBOARDING_COMPLETED, false)
                putBoolean(KEY_PROFILE_COMPLETE, false)
                apply()
            }

            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            startActivity(Intent(this@HomeActivity, SignInActivity::class.java))
            finish()
        }
    }
}
