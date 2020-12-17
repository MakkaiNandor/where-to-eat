package com.example.androidproject.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.R
import com.example.androidproject.RestaurantAdapter
import com.example.androidproject.activity.MainActivity
import com.example.androidproject.api.model.Restaurant
import com.example.androidproject.database.DbViewModel
import com.example.androidproject.database.DbViewModelFactory

class ProfileFragment : Fragment(), RestaurantAdapter.OnItemClickListener {

    private lateinit var dbViewModel: DbViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        dbViewModel = ViewModelProvider(this, DbViewModelFactory(requireActivity().application)).get(DbViewModel::class.java)

        // Display user's personal data
        setUpPersonalData(root)

        // Display user's favorite restaurants
        val favoriteList = root.findViewById<RecyclerView>(R.id.favorites_list)
        val adapter = RestaurantAdapter(requireContext(), this)
        val userFavorites = dbViewModel.getUserFavorites()
        adapter.setRestaurants(userFavorites as MutableList<Restaurant>)
        adapter.setUserFavorites(userFavorites.map { it.id } as MutableList<Long>)
        favoriteList.adapter = adapter
        favoriteList.layoutManager = LinearLayoutManager(requireContext())
        favoriteList.setHasFixedSize(true)

        return root
    }

    private fun setUpPersonalData(view: View){
        view.findViewById<TextView>(R.id.name_value).text = MainActivity.loggedInUser?.name
        view.findViewById<TextView>(R.id.email_value).text = MainActivity.loggedInUser?.email
        view.findViewById<TextView>(R.id.address_value).text = MainActivity.loggedInUser?.address
        view.findViewById<TextView>(R.id.phone_value).text = MainActivity.loggedInUser?.phone
    }

    override fun onItemClick(position: Int) {

    }

    override fun onFavIconClick(item: Restaurant, favorite: Boolean) {
        if(favorite){
            dbViewModel.addUserFavorite(item)
        }
        else{
            dbViewModel.removeUserFavorite(item)
        }
    }

}