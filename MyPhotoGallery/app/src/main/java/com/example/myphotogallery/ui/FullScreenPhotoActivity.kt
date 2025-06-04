package com.example.myphotogallery.ui

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myphotogallery.adapter.FullScreenImageAdapter
import com.example.myphotogallery.databinding.ActivityFullScreenPhotoBinding

class FullScreenPhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullScreenPhotoBinding
    private var currentPosition: Int = 0
    private lateinit var photoUris: ArrayList<Uri>


    companion object {
        const val EXTRA_PHOTO_URIS = "extra_photo_uris"
        const val EXTRA_CURRENT_POSITION = "extra_current_position"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullScreenPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve data from Intent
        currentPosition = intent.getIntExtra(EXTRA_CURRENT_POSITION, 0)

        photoUris = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra(EXTRA_PHOTO_URIS, Uri::class.java) ?: arrayListOf()
        } else {
            @Suppress("DEPRECATION") // Needed for older SDKs
            intent.getParcelableArrayListExtra(EXTRA_PHOTO_URIS) ?: arrayListOf()
        }


        if (photoUris.isNotEmpty()) {
            val adapter = FullScreenImageAdapter(photoUris)
            binding.viewPagerFullScreen.adapter = adapter
            binding.viewPagerFullScreen.setCurrentItem(currentPosition, false) // false for no smooth scroll animation
        } else {
            // Handle the case where no URIs are passed, perhaps finish the activity
            finish()
        }

        // Optional: Hide system bars for a more immersive experience
        // WindowCompat.setDecorFitsSystemWindows(window, false)
        // WindowInsetsControllerCompat(window, binding.root).let { controller ->
        //     controller.hide(WindowInsetsCompat.Type.systemBars())
        //     controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // }
    }
}