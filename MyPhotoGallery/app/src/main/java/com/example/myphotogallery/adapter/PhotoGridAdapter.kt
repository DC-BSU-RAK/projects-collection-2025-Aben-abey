package com.example.myphotogallery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myphotogallery.databinding.ItemPhotoThumbnailBinding
import com.example.myphotogallery.model.PhotoItem

class PhotoGridAdapter(
    private val onPhotoClick: (photo: PhotoItem, position: Int) -> Unit
) : ListAdapter<PhotoItem, PhotoGridAdapter.PhotoViewHolder>(PhotoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPhotoThumbnailBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photoItem = getItem(position)
        holder.bind(photoItem)
        holder.itemView.setOnClickListener {
            onPhotoClick(photoItem, position)
        }
    }

    inner class PhotoViewHolder(private val binding: ItemPhotoThumbnailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: PhotoItem) {
            Glide.with(binding.imageViewThumbnail.context)
                .load(photo.uri)
                .placeholder(android.R.drawable.ic_menu_gallery) // Optional placeholder
                .error(android.R.drawable.ic_menu_report_image) // Optional error image
                .centerCrop()
                .into(binding.imageViewThumbnail)
        }
    }

    class PhotoDiffCallback : DiffUtil.ItemCallback<PhotoItem>() {
        override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem == newItem
        }
    }
}