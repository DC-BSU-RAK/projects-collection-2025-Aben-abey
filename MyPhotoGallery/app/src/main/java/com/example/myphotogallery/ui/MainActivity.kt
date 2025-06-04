package com.example.myphotogallery.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import android.util.Log
import com.example.myphotogallery.R // Ensure R is imported
import com.example.myphotogallery.adapter.PhotoGridAdapter
import com.example.myphotogallery.databinding.ActivityMainBinding
// import com.example.myphotogallery.model.PhotoItem // Not directly used here anymore for intent if uris are directly passed
import com.example.myphotogallery.viewmodel.GalleryViewModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: GalleryViewModel by viewModels()
    private lateinit var photoAdapter: PhotoGridAdapter

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                viewModel.onPermissionGranted()
            } else {
                viewModel.onPermissionDenied()
                showPermissionDeniedSnackbar()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar and Bottom Navigation setup calls are removed
        setupSettingsButton()
        setupRecyclerView()
        observeViewModel()
        checkAndRequestPermissions()
    }

    private fun setupRecyclerView() {
        photoAdapter = PhotoGridAdapter { _, position -> // photoItem not needed if only passing URI list
            val photoUris = viewModel.photos.value?.map { it.uri } ?: emptyList()
            if (photoUris.isNotEmpty()) {
                val intent = Intent(this, FullScreenPhotoActivity::class.java).apply {
                    putParcelableArrayListExtra(FullScreenPhotoActivity.EXTRA_PHOTO_URIS, ArrayList(photoUris))
                    putExtra(FullScreenPhotoActivity.EXTRA_CURRENT_POSITION, position)
                }
                startActivity(intent)
            }
        }
        binding.recyclerViewPhotos.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2) // 2 columns
            adapter = photoAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.permissionGranted.observe(this) { isGranted ->
            if (isGranted) {
                binding.textViewMessage.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE // Show progress bar while loading
            } else {
                binding.recyclerViewPhotos.visibility = View.GONE
                binding.textViewMessage.visibility = View.VISIBLE
                binding.textViewMessage.text = getString(R.string.permission_required_message)
                binding.progressBar.visibility = View.GONE
            }
        }

        viewModel.photos.observe(this) { photos ->
            binding.progressBar.visibility = View.GONE // Hide progress bar once photos are loaded (or not found)
            if (photos.isNotEmpty()) {
                photoAdapter.submitList(photos)
                binding.recyclerViewPhotos.visibility = View.VISIBLE
                binding.textViewMessage.visibility = View.GONE
            } else if (viewModel.permissionGranted.value == true) { // Permission granted but no photos
                binding.recyclerViewPhotos.visibility = View.GONE
                binding.textViewMessage.visibility = View.VISIBLE
                binding.textViewMessage.text = getString(R.string.no_photos_found)
            }
        }
    }

    private fun checkAndRequestPermissions() {
        val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(this, permissionToRequest) == PackageManager.PERMISSION_GRANTED -> {
                viewModel.onPermissionGranted()
            }
            shouldShowRequestPermissionRationale(permissionToRequest) -> {
                Snackbar.make(binding.root, getString(R.string.storage_permission_rationale), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.grant_permission)) {
                        requestPermissionLauncher.launch(permissionToRequest)
                    }.show()
                viewModel.onPermissionDenied()
            }
            else -> {
                requestPermissionLauncher.launch(permissionToRequest)
            }
        }
    }

    private fun showPermissionDeniedSnackbar() {
        Snackbar.make(binding.root, getString(R.string.permission_denied_message), Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.settings_text)) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }.show()
    }
    private fun setupSettingsButton() {
        Log.d("MainActivity", "setupSettingsButton called") // Check if method is called
        binding.imageButtonSettings.setOnClickListener {
            Log.d("MainActivity", "Settings button clicked!") // Check if click is registered
            try {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                Log.d("MainActivity", "startActivity(SettingsActivity) called")
            } catch (e: Exception) {
                Log.e("MainActivity", "Error starting SettingsActivity", e) // Check for errors
            }
        }
    }
}