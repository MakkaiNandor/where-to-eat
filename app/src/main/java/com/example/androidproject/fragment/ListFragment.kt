package com.example.androidproject.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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
) : Fragment(), RestaurantAdapter.OnItemClickListener, RestaurantAdapter.OnBottomReachedListener {

    private lateinit var apiViewModel: ApiViewModel
    private lateinit var dbViewModel: DbViewModel
    private lateinit var searchBox: View
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var restaurantList: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_list, container, false)

        dbViewModel = ViewModelProvider(this, DbViewModelFactory(requireActivity().application)).get(DbViewModel::class.java)
        apiViewModel = ViewModelProvider(this, ApiViewModelFactory(ApiRepository())).get(ApiViewModel::class.java)

        Log.d("DEBUG", "Logged in user: ${MainActivity.loggedInUser?.name}")

        // Set up message about filters
        val filterInfoText = "Filters:\nName: ${MainActivity.filters["name"] ?: "ALL"}, Display: ${MainActivity.displayType}\nCity: ${MainActivity.filters["city"] ?: "ALL"}, Price: ${MainActivity.filters["price"] ?: "ALL"}"
        root.findViewById<TextView>(R.id.filter_info).text = filterInfoText

        // Set up recycler view
        restaurantList = root.findViewById<RecyclerView>(R.id.rest_list)
        restaurantAdapter = RestaurantAdapter(requireContext(), this)
        restaurantAdapter.setOnBottomReachedListener(this)
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
            MainActivity.filters = MainActivity.filters.minus("page")
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

    /**
     * Load the next page's data, when bottom of list reached
     */
    override fun onBottomReached(position: Int) {
        if(isNetworkAvailable()){
            if(MainActivity.currentPage < MainActivity.pages) {
                // Loading next page's restaurants and add them to adapter
                Toast.makeText(requireContext(), "Loading data", Toast.LENGTH_LONG).show()
                MainActivity.filters = MainActivity.filters.plus("page" to "${++MainActivity.currentPage}")
                apiViewModel.getAllRestaurants(MainActivity.filters)
                apiViewModel.restaurantsResponse.observe(viewLifecycleOwner, Observer { response ->
                    if (response.isSuccessful) {
                        Log.d("DEBUG", "Restaurants successfully received")
                        var restaurants: List<Restaurant> = if (response.body() == null) listOf() else response.body()!!.restaurants
                        var favorites: List<Restaurant> = dbViewModel.getUserFavorites()
                        if (MainActivity.displayType == DisplayType.FAVORITES) {
                            val filterName = MainActivity.filters["name"]
                            val filterCity = MainActivity.filters["city"]
                            val filterPrice = MainActivity.filters["price"]
                            if (filterName != null) favorites = favorites.filter { it.name.contains(filterName, true) }
                            if (filterCity != null) favorites = favorites.filter { it.city == filterCity }
                            if (filterPrice != null) favorites = favorites.filter { it.price == filterPrice.toInt() }
                            restaurants = favorites
                        } else if (MainActivity.displayType == DisplayType.WITHOUT_FAVORITES) {
                            val favoritesId: List<Long> = favorites.map { it.id }
                            restaurants = restaurants.filter { !favoritesId.contains(it.id) }
                        }
                        restaurantAdapter.addRestaurants(restaurants)
                        restaurantList.post {
                            restaurantAdapter.notifyDataSetChanged()
                        }
                        //restaurantAdapter.notifyDataSetChanged()
                    } else {
                        // Display error message
                        Toast.makeText(requireContext(), "Error ${response.code()}", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
        else{
            Toast.makeText(requireContext(), "No internet connection!", Toast.LENGTH_LONG).show()
        }
    }

    private fun isNetworkAvailable() : Boolean {
        val manager =  requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val network = manager.activeNetwork ?: return false
            val activeNetwork = manager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        }
        else {
            return manager.activeNetworkInfo?.isConnected ?: false
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