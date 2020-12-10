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
import com.example.androidproject.api.RetrofitInstance.api
import com.example.androidproject.activity.MainActivity
import com.example.androidproject.api.ApiRepository
import com.example.androidproject.api.DataViewModel
import com.example.androidproject.api.DataViewModelFactory
import com.example.androidproject.api.model.Restaurants
import retrofit2.Call
import retrofit2.Response

class LoadingFragment : Fragment() {

    private lateinit var viewModel: DataViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_loading, container, false)

        val repository = ApiRepository()
        viewModel = ViewModelProvider(this, DataViewModelFactory(repository)).get(DataViewModel::class.java)

        // Error message view
        val errorMessageView = root.findViewById<TextView>(R.id.error)

        // Check internet connection
        val connection = isNetworkAvailable()

        if(connection){
            // Internet connection is OK
            // Get restaurants with filters
            viewModel.getAllRestaurants(MainActivity.filters)
            viewModel.restaurantsResponse.observe(this, Observer {response ->
                if(response.isSuccessful){
                    Log.d("Response", "Successful: ${response.body()!!.restaurants.size}")
                    MainActivity.restaurants = response.body()!!.restaurants
                    fragmentManager?.beginTransaction()?.replace(
                            R.id.fragment_container,
                            ListFragment()
                    )?.commit()
                }
                else{
                    Log.d("Response", "Failure: ${response.code()}")
                    errorMessageView.text = response.errorBody().toString()
                }
            })
        }
        else{
            // No internet connection
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
        val manager =  activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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