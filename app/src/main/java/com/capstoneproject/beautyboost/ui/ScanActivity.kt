package com.capstoneproject.beautyboost.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstoneproject.beautyboost.databinding.ActivityScanBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding

    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null

    val auth = Firebase.auth
    val user = auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (user != null) {
            val userName = user.displayName?.split(" ")?.get(0) ?: "User"
            binding.name.text = "Hi, " + userName + "  \uD83D\uDC4B"
        } else {
            // Handle the case when the user is not signed in
        }

        binding.dropzone.setOnClickListener {
            openGallery()
        }

        binding.removeImageIcon.setOnClickListener {
            removeImage()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun removeImage() {
        binding.imagePreview.setImageURI(null)
        binding.imagePreview.visibility = View.GONE
        binding.removeImageIcon.visibility = View.INVISIBLE
        binding.cameraIcon.visibility = View.VISIBLE
        binding.textView.visibility = View.VISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                imageUri = it
                displayImage()
            } ?: run {
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayImage() {
        imageUri?.let {
            binding.imagePreview.setImageURI(it)
            binding.imagePreview.visibility = View.VISIBLE
            binding.removeImageIcon.visibility = View.VISIBLE
            binding.cameraIcon.visibility = View.GONE
            binding.textView.visibility = View.GONE
        }
    }
}
