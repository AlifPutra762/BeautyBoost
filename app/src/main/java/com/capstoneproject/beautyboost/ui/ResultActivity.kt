package com.capstoneproject.beautyboost.ui

//import android.net.Uri
import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstoneproject.beautyboost.R
import com.capstoneproject.beautyboost.databinding.ActivityResultBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File


class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        val filePath = getRealPathFromUri(contentResolver, imageUri)
        val file = File(filePath)

        binding.animLoading.setAnimation(R.raw.anim)
//        binding.cvRekomendasi.visibility = android.view.View.VISIBLE

        Log.d("ResultActivity", "File path: ${imageUri}")



//        val file = File(imageUri.path!!)
        Log.d("ResultActivity", "File path: ${file.canonicalPath}")

//        val file_name = "original.png"
//        val file = File(file_name)
//        println(file.getCanonicalPath())

        binding.resultImage.setImageURI(imageUri)

        val client = OkHttpClient.Builder().connectTimeout(100, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(100, java.util.concurrent.TimeUnit.SECONDS).build()
        val url = "https://predict-snaqemxh3a-as.a.run.app"
        val request = Request.Builder()
            .url(url)
            .post(
                MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(
                        "file",
                        getFileName(contentResolver, imageUri)!!,
                        file.asRequestBody("image/*".toMediaType())
                    )
                    .build()
            )
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                Log.e("ResultActivity", "Error uploading image", e)
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val body = response.body?.string()
                val result = JSONObject(body)
                val totalAcne = result.getInt("Number of valid boxes")
                Log.d("ResultActivity", "Response: $result")
                runOnUiThread {
//                    binding.resultText.text = "Total acne: $totalAcne"
                    showToast(body.toString())
                    setImageMeter(totalAcne)
                }
            }
        })

    }

    fun setImageMeter(totalAcne : Int) {
        binding.tvTitleRecomendasi.visibility = android.view.View.VISIBLE
        binding.resultDesc.visibility = android.view.View.VISIBLE
        binding.progressBar.visibility = android.view.View.GONE

        if (totalAcne < 5) {
//            binding.resultText.text = "LOW: $totalAcne"
            binding.resultMeter.setImageResource(R.drawable.low)
            binding.resultDesc.text = getString(R.string.low)
        } else if (totalAcne < 10) {
//            binding.resultText.text = "MEDIUM: $totalAcne"
            binding.resultMeter.setImageResource(R.drawable.mid)
            binding.resultDesc.text = getString(R.string.mid)

        } else {
//            binding.resultText.text = "HIGH: $totalAcne"
            binding.resultMeter.setImageResource(R.drawable.high)
            binding.resultDesc.text = getString(R.string.high)
        }

        binding.ivProduk.setImageResource(R.drawable.avoskin)
        binding.tvProduk.text = "Obat Jerawat Salicylic Acid"
        binding.ivProduk2.setImageResource(R.drawable.azarine)
        binding.tvProduk2.text = "Azarine Acne Gentle Cleanser"

        binding.animLoading.cancelAnimation()
        binding.llAnim.visibility = android.view.View.GONE
    }

    fun getRealPathFromUri(contentResolver: ContentResolver, uri: Uri): String? {
        var filePath: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                filePath = it.getString(columnIndex)
            }
        }
        cursor?.close()
        return filePath
    }

    fun getFileName(contentResolver: ContentResolver, uri: Uri): String? {
        var fileName: String? = null
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                fileName = cursor.getString(displayNameIndex)
            }
        }
        return fileName
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    companion object{
        const val EXTRA_IMAGE_URI = "image_uri"
    }

}