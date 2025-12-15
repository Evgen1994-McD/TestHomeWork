package com.example.testhomework.presentation.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testhomework.databinding.ItemPhotoBinding
import com.example.testhomework.domain.model.Photo

class PhotosAdapter(
    private val onClick: (Photo) -> Unit
) : ListAdapter<Photo, PhotosAdapter.PhotoViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPhotoBinding.inflate(inflater, parent, false)
        return PhotoViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PhotoViewHolder(
        private val binding: ItemPhotoBinding,
        private val onClick: (Photo) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: Photo) {
            Glide.with(binding.imageView)
                .load(photo.thumbnailUrl)
                .centerCrop()
                .into(binding.imageView)

            binding.root.setOnClickListener {
                onClick(photo)
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean =
            oldItem == newItem
    }
}


