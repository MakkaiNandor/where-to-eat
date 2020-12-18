package com.example.androidproject.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.R
import com.example.androidproject.RestaurantAdapter
import com.example.androidproject.activity.DisplayType
import com.example.androidproject.activity.MainActivity
import com.example.androidproject.api.ApiRepository
import com.example.androidproject.api.ApiViewModel
import com.example.androidproject.api.ApiViewModelFactory
import com.example.androidproject.api.model.Restaurant
import com.example.androidproject.database.DbViewModel
import com.example.androidproject.database.DbViewModelFactory

class ListFragment(
        private val listOfRestaurants: List<Restaurant>,
        private val listOfFavorites: List<Restaurant>
) : Fragment(), RestaurantAdapter.OnItemClickListener {

    private lateinit var dbViewModel: DbViewModel
    private lateinit var searchBox: View
    private lateinit var restaurantAdapter: RestaurantAdapter
    private var pageNumber = 1;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_list, container, false)

        dbViewModel = ViewModelProvider(this, DbViewModelFactory(requireActivity().application)).get(DbViewModel::class.java)

        Log.d("DEBUG", "Logged in user: ${MainActivity.loggedInUser?.name}")

        // Set up message about filters
        val filterInfoText = "Filters:\nName: ${MainActivity.filters["name"] ?: "ALL"}, Display: ${MainActivity.displayType}\nCity: ${MainActivity.filters["city"] ?: "ALL"}, Price: ${MainActivity.filters["price"] ?: "ALL"}"
        root.findViewById<TextView>(R.id.filter_info).text = filterInfoText

        // Set up recycler view
        val restaurantList = root.findViewById<RecyclerView>(R.id.rest_list)
        restaurantAdapter = RestaurantAdapter(requireContext(), this)
        restaurantAdapter.setRestaurants(listOfRestaurants)
        restaurantAdapter.setUserFavorites(listOfFavorites.map { it.id })
        restaurantList.adapter = restaurantAdapter
        restaurantList.layoutManager = LinearLayoutManager(requireContext())
        restaurantList.setHasFixedSize(true)

        //Set up search box
        searchBox = root.findViewById<ConstraintLayout>(R.id.search_box)
        val searchInput: EditText = searchBox.findViewById(R.id.search_input)
        searchBox.findViewById<Button>(R.id.search_btn).setOnClickListener{
            if(searchInput.text.isBlank()){
                MainActivity.filters = MainActivity.filters.minus("name")
            }
            else{
                MainActivity.filters = MainActivity.filters.plus("name" to searchInput.text.toString().trim())
            }
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragment_container_main, LoadingFragment()).commit()
        }

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
            R.id.search_item -> {
                searchBox.visibility = if(searchBox.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                true
            }
            else -> false
        }
    }

}