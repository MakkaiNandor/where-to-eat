package com.example.androidproject

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidproject.api.model.Restaurant

class RestaurantAdapter(
    private val context: Context,
    private val clickListener: OnItemClickListener
) : RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    interface OnItemClickListener{
        fun onItemClick(position: Int)
        fun onFavIconClick(item: Restaurant, favorite: Boolean)
    }

    interface OnBottomReachedListener{
        fun onBottomReached(position: Int)
    }

    private var restaurants: MutableList<Restaurant> = mutableListOf()
    private var userFavorites: MutableList<Long> = mutableListOf()
    private var scrollListener: OnBottomReachedListener? = null

    fun setOnBottomReachedListener(listener: OnBottomReachedListener){
        scrollListener = listener
    }

    inner class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val restImageView: ImageView = itemView.findViewById(R.id.rest_img)
        val nameTextView: TextView = itemView.findViewById(R.id.rest_name)
        val addressTextView: TextView = itemView.findViewById(R.id.rest_address)
        val priceTextView: TextView = itemView.findViewById(R.id.rest_price)
        val favImageView: ImageButton = itemView.findViewById(R.id.favorite_btn)

        init {
            itemView.setOnClickListener(this)
            favImageView.setOnClickListener (this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                if(v?.id == R.id.favorite_btn){
                    val favIcon = v as ImageButton
                    val id = restaurants[position].id
                    if(userFavorites.contains(id)){
                        favIcon.setImageResource(android.R.drawable.btn_star_big_off)
                        userFavorites.remove(id)
                        clickListener.onFavIconClick(restaurants[position], false)
                    }
                    else{
                        favIcon.setImageResource(android.R.drawable.btn_star_big_on)
                        userFavorites.add(id)
                        clickListener.onFavIconClick(restaurants[position], true)
                    }
                    Log.d("DEBUG", "Favorites: ${userFavorites.size}")
                }
                else {
                    clickListener.onItemClick(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return RestaurantViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val currentItem = restaurants[position]
        //Glide.with(context).load(currentItem.image_url).into(holder.restImageView)
        holder.nameTextView.text = currentItem.name
        val addressText = "${currentItem.city}, ${currentItem.address}"
        holder.addressTextView.text = addressText
        val priceText = "Price: ${currentItem.price}"
        holder.priceTextView.text = priceText
        if(userFavorites.contains(currentItem.id)){
            holder.favImageView.setImageResource(android.R.drawable.btn_star_big_on)
        }
        else {
            holder.favImageView.setImageResource(android.R.drawable.btn_star_big_off)
        }
        if(position == restaurants.size - 1){
            scrollListener?.onBottomReached(position)
        }
    }

    override fun getItemCount(): Int = restaurants.size

    fun setRestaurants(list: List<Restaurant>){
        this.restaurants = list as MutableList<Restaurant>
        notifyDataSetChanged()
    }

    fun addRestaurants(newList: List<Restaurant>){
        this.restaurants.addAll(newList)
        //notifyDataSetChanged()
    }

    fun setUserFavorites(list: List<Long>){
        this.userFavorites = list as MutableList<Long>
    }

}