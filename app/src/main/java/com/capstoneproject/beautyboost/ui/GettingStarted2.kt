package com.capstoneproject.beautyboost.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.capstoneproject.beautyboost.R
import com.capstoneproject.beautyboost.data.KEY_ONBOARDING_COMPLETED
import com.capstoneproject.beautyboost.data.PREFS_NAME

class GettingStarted2 : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        enableEdgeToEdge()
        setContentView(R.layout.activity_getting_started2)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnNext: Button = findViewById(R.id.button_getting_started)
        btnNext.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_getting_started -> {

                val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

                with(sharedPreferences.edit()) {
                    putBoolean(KEY_ONBOARDING_COMPLETED, true)
                    apply()
                }

                val moveIntent = Intent(this@GettingStarted2, SignInActivity::class.java)
                startActivity(moveIntent)
                finish()
            }
        }
    }
}