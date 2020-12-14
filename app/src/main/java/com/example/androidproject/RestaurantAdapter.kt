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
import kotlinx.android.synthetic.main.item_layout.view.*

class RestaurantAdapter(private val context: Context, private val listener: OnItemClickListener) : RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    private var list: List<Restaurant> = listOf()

    inner class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val restImageView: ImageView = itemView.findViewById(R.id.rest_img)
        val nameTextView: TextView = itemView.findViewById(R.id.rest_name)
        val addressTextView: TextView = itemView.findViewById(R.id.rest_address)
        val priceTextView: TextView = itemView.findViewById(R.id.rest_price)
        val favImageView: ImageView = itemView.findViewById(R.id.favorite_btn)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            Log.d("DEBUG", v.toString())
            val position: Int = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return RestaurantViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val currentItem = list[position]
        Glide.with(context)
            .load(currentItem.image_url)
            .into(holder.restImageView)
        holder.nameTextView.text = currentItem.name
        val addressText = "${currentItem.city}, ${currentItem.address}"
        holder.addressTextView.text = addressText
        val priceText = "Price: ${currentItem.price}"
        holder.priceTextView.text = priceText
        holder.favImageView.setImageResource(android.R.drawable.star_big_off)
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