package com.capstoneproject.beautyboost.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstoneproject.beautyboost.databinding.ActivityHomeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

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
    }

    private fun getArticlesList(): List<Article> {
        // Dummy data, replace with real data fetching logic
        return listOf(
            Article("Article 1", "Description 1"),
            Article("Article 2", "Description 2"),
            Article("Article 3", "Description 3"),
            Article("Article 4", "Description 4"),
            Article("Article 5", "Description 5")
        )
    }
}
