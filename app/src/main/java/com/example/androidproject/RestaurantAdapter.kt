package com.example.androidproject

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidproject.api.model.Restaurant

class RestaurantAdapter(private val context: Context) : RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    private var list: List<Restaurant> = listOf()

    class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val restImageView: ImageView = itemView.findViewById(R.id.rest_img)
        val nameTextView: TextView = itemView.findViewById(R.id.rest_name)
        val addressTextView: TextView = itemView.findViewById(R.id.rest_address)
        val priceTextView: TextView = itemView.findViewById(R.id.rest_price)
        val favImageView: ImageView = itemView.findViewById(R.id.favorite_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return RestaurantViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val currentItem = list[position]
//        Log.d("DEBUG", "Name: ${currentItem.name}")
//        Log.d("DEBUG", "Address: ${currentItem.address}")
//        Log.d("DEBUG", "Price: ${currentItem.price}")
        Glide.with(context).load(currentItem.image_url).into(holder.restImageView)
        holder.nameTextView.text = currentItem.name
        holder.addressTextView.text = currentItem.address
        val priceText = "Price: ${currentItem.price}"
        holder.priceTextView.text = priceText
    }

    override fun getItemCount(): Int = list.size

    fun setList(list: List<Restaurant>){
        this.list = list
        notifyDataSetChanged()
    }

    fun addData(newList: List<Restaurant>){
        this.list += newList
        notifyDataSetChanged()
    }

}