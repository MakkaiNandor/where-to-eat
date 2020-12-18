package com.example.androidproject.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.example.androidproject.R
import com.example.androidproject.api.model.Restaurant

class DetailFragment(private val restaurant: Restaurant) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_detail, container, false)

        val restaurantDataContainer: ConstraintLayout = root.findViewById(R.id.default_data_section)
        displayRestaurantData(restaurantDataContainer)



        return root
    }

    private fun displayRestaurantData(parent: View){
        parent.findViewById<TextView>(R.id.restaurant_name).text = restaurant.name
        Glide.with(requireContext()).load(restaurant.image_url).into(parent.findViewById(R.id.restaurant_image))
        parent.findViewById<TextView>(R.id.price_value).text = restaurant.price.toString()
        parent.findViewById<TextView>(R.id.country_value).text = restaurant.country
        parent.findViewById<TextView>(R.id.state_value).text = restaurant.state
        parent.findViewById<TextView>(R.id.area_value).text = restaurant.area
        parent.findViewById<TextView>(R.id.city_value).text = restaurant.city
        parent.findViewById<TextView>(R.id.address_value).text = restaurant.address
        parent.findViewById<TextView>(R.id.postal_code_value).text = restaurant.postal_code
        parent.findViewById<TextView>(R.id.phone_value).text = restaurant.phone
    }

}