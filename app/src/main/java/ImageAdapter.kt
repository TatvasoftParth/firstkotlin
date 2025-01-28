package com.example.demoapp

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.viewsandevents.databinding.GridItemBinding
import java.io.File

class ImageAdapter(private val context: Context, private val images: List<File?>) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(val binding: GridItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = GridItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageFile = images[position]
        Glide.with(holder.itemView.context)
            .load(Uri.fromFile(imageFile))
            .into(holder.binding.imageView)
    }

    override fun getItemCount(): Int = images.size
}
