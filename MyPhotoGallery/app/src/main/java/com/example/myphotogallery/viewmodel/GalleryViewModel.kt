package com.example.myphotogallery.viewmodel

import android.app.Application
import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myphotogallery.model.PhotoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GalleryViewModel(application: Application) : AndroidViewModel(application) {

    private val _photos = MutableLiveData<List<PhotoItem>>()
    val photos: LiveData<List<PhotoItem>> = _photos

    private val _permissionGranted = MutableLiveData<Boolean>()
    val permissionGranted: LiveData<Boolean> = _permissionGranted

    // Call this when permission is granted
    fun onPermissionGranted() {
        _permissionGranted.value = true
        loadPhotos()
    }

    // Call this when permission is denied
    fun onPermissionDenied() {
        _permissionGranted.value = false
    }

    private fun loadPhotos() {
        viewModelScope.launch {
            val photoList = queryPhotosFromDevice()
            _photos.postValue(photoList)
        }
    }

    private suspend fun queryPhotosFromDevice(): List<PhotoItem> = withContext(Dispatchers.IO) {
        val photoItems = mutableListOf<PhotoItem>()
        val appContext = getApplication<Application>().applicationContext

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED
        )

        // Determine the URI to query based on Android version for broader compatibility
        val queryUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        // Sort order
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        appContext.contentResolver.query(
            queryUri,
            projection,
            null, // No selection (null will include all images)
            null, // No selection arguments
            sortOrder
        )?.use { cursor -> // 'use' ensures the cursor is closed automatically
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val displayName = cursor.getString(displayNameColumn)
                val dateAdded = cursor.getLong(dateAddedColumn)
                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                photoItems.add(PhotoItem(id, contentUri, displayName, dateAdded))
            }
        }
        return@withContext photoItems
    }
}