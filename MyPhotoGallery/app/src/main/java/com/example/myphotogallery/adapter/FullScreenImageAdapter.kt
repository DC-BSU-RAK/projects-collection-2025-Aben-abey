package com.example.myphotogallery.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myphotogallery.databinding.ItemFullScreenImageBinding

class FullScreenImageAdapter(private val imageUris: List<Uri>) :
    RecyclerView.Adapter<FullScreenImageAdapter.FullScreenViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FullScreenViewHolder {
        val binding = ItemFullScreenImageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FullScreenViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FullScreenViewHolder, position: Int) {
        holder.bind(imageUris[position])
    }

    override fun getItemCount(): Int = imageUris.size

    inner class FullScreenViewHolder(private val binding: ItemFullScreenImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUri: Uri) {
            Glide.with(binding.imageViewFullScreen.context)
                .load(imageUri)
                .fitCenter() // Ensure the image fits within the bounds, maintaining aspect ratio
                .error(android.R.drawable.ic_menu_report_image) // Optional error placeholder
                .into(binding.imageViewFullScreen)
        }
    }
}