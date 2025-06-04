package com.example.myphotogallery.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize // To easily pass PhotoItem objects or lists between activities/fragments
data class PhotoItem(
    val id: Long,
    val uri: Uri,
    val displayName: String,
    val dateAdded: Long
) : Parcelable