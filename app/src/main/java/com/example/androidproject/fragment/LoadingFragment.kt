package com.example.androidproject.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.R
import com.example.androidproject.activity.DisplayType
import com.example.androidproject.activity.MainActivity
import com.example.androidproject.api.ApiRepository
import com.example.androidproject.api.ApiViewModel
import com.example.androidproject.api.ApiViewModelFactory
import com.example.androidproject.api.model.Restaurant
import com.example.androidproject.database.DbViewModel
import com.example.androidproject.database.DbViewModelFactory

class LoadingFragment : Fragment() {

    private lateinit var apiViewModel: ApiViewModel
    private lateinit var errorMessageView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_loading, container, false)

        apiViewModel = ViewModelProvider(this, ApiViewModelFactory(ApiRepository())).get(ApiViewModel::class.java)

        // Error message view
        errorMessageView = root.findViewById(R.id.error)

        // Check internet connection
        if(isNetworkAvailable()){
            // Internet connection is OK
            // Get restaurants with filters and user's favorites
            Log.d("DEBUG", "Internet connection OK")
            apiViewModel.getAllRestaurants(MainActivity.filters)
            apiViewModel.restaurantsResponse.observe(viewLifecycleOwner, Observer { response ->
                if(response.isSuccessful){
                    Log.d("DEBUG", "Restaurants successfully received")
                    val restaurants: List<Restaurant> = if(response.body() == null) listOf() else response.body()!!.restaurants
                    // Go to List fragment
                    requireActivity().supportFragmentManager.beginTransaction().replace(
                        R.id.fragment_container_main,
                        ListFragment(restaurants)
                    ).commit()
                }
                else{
                    // Display error message
                    val errMsg = "Error ${response.code()}: ${response.errorBody()}"
                    Log.d("Response", errMsg)
                    errorMessageView.text = errMsg
                }
            })
        }
        else{
            // No internet connection
            Log.d("DEBUG", "No internet connection")
            val errMsg = "No internet connection!"
            errorMessageView.text = errMsg
        }

        return root
    }

    /**
     * Check the internet connection
     *
     * @return True or False
     */
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

}