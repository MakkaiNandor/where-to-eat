package com.example.androidproject.adapter

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.R

class ImageAdapter(
        private val context: Context,
        private val images: MutableList<Bitmap>,
        private val listener: OnItemLongClickListener
): RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    interface OnItemLongClickListener{
        fun onItemLongClick(item: Bitmap)
    }

    inner class ImageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnLongClickListener {
        val imageHolder: ImageView = itemView.findViewById(R.id.image_item)

        init {
            itemView.setOnLongClickListener(this)
        }

        override fun onLongClick(v: View?): Boolean {
            val position: Int = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                val alert = AlertDialog.Builder(context)
                alert.setTitle("Image deletion!")
                alert.setMessage("Are you delete this image?")
                alert.setNegativeButton("No", null)
                alert.setPositiveButton("Yes") {_, _ ->
                    val item = images[position]
                    images.removeAt(position)
                    listener.onItemLongClick(item)
                    notifyItemRemoved(position)
                }
                alert.create().show()
            }
            return true
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.image_layout, parent, false)
        return ImageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentItem = images[position]
        holder.imageHolder.setImageBitmap(currentItem)
    }

    override fun getItemCount(): Int = images.size

    fun addImage(image: Bitmap){
        this.images.add(image)
        notifyDataSetChanged()
    }

}