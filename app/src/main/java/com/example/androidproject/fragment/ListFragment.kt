package com.example.androidproject.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.R
import com.example.androidproject.RestaurantAdapter
import com.example.androidproject.activity.MainActivity
import com.example.androidproject.api.ApiRepository
import com.example.androidproject.api.ApiViewModel
import com.example.androidproject.api.ApiViewModelFactory
import com.example.androidproject.api.model.Restaurant
import com.example.androidproject.database.DbViewModel
import com.example.androidproject.database.DbViewModelFactory

class ListFragment(private val listOfRestaurants: List<Restaurant>) : Fragment(), RestaurantAdapter.OnItemClickListener {

    private lateinit var dbViewModel: DbViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_list, container, false)

        dbViewModel = ViewModelProvider(this, DbViewModelFactory(requireActivity().application)).get(DbViewModel::class.java)

        Log.d("DEBUG", "Logged in user: ${MainActivity.loggedInUser?.name}")

        // Set up message about filters
        val filterInfoText = "Filters:\nDisplay: ${MainActivity.displayType}, City: ${MainActivity.filters["city"] ?: "ALL"}, Price: ${MainActivity.filters["price"] ?: "ALL"}"
        root.findViewById<TextView>(R.id.filter_info).text = filterInfoText

        // Set up recycler view
        val restaurantList = root.findViewById<RecyclerView>(R.id.rest_list)
        val adapter = RestaurantAdapter(requireContext(), this)
        adapter.setRestaurants(listOfRestaurants)
        adapter.setUserFavorites(dbViewModel.getUserFavorites().map { it.id })
        restaurantList.adapter = adapter
        restaurantList.layoutManager = LinearLayoutManager(requireContext())
        restaurantList.setHasFixedSize(true)

        return root
    }

    override fun onItemClick(position: Int) {
        Log.d("DEBUG", "Item $position clicked")
    }

    override fun onFavIconClick(item: Restaurant, favorite: Boolean) {
        if(favorite){
            dbViewModel.addUserFavorite(item)
        }
        else{
            dbViewModel.removeUserFavorite(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enable options menu in action bar
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Own action bar menu
        inflater.inflate(R.menu.list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Actions for option items
        return when (item.itemId) {
            R.id.filter_item -> {
                requireActivity().supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container_main,
                    FilterFragment()
                ).commit()
                true
            }
            else -> false
        }
    }

}