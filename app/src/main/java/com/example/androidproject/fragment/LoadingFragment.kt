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
import com.example.androidproject.R
import com.example.androidproject.retrofit.RetrofitInstance.api
import com.example.androidproject.activity.MainActivity
import com.example.androidproject.retrofit.model.Restaurants
import retrofit2.Call
import retrofit2.Response

class LoadingFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_loading, container, false)

        // Error message view
        val errorMessageView = root.findViewById<TextView>(R.id.error)

        // Check internet connection
        val connection = isNetworkAvailable()

        if(connection){
            // Internet connection is OK
            // Get restaurants with filters
            api.getRestaurants(MainActivity.filters).enqueue(object : retrofit2.Callback<Restaurants>{
                override fun onResponse(call: Call<Restaurants>, response: Response<Restaurants>) {
                    if(response.isSuccessful) {
                        // Save the list of restaurants and redirect to list fragment
                        MainActivity.restaurants = response.body()!!.restaurants
                        fragmentManager?.beginTransaction()?.replace(
                                R.id.fragment_container,
                                ListFragment()
                        )?.commit()
                    }
                    else{
                        // Show error message
                        val errorMsg = "Error ${response.code()}: ${response.message()}"
                        errorMessageView.text = errorMsg
                    }
                }

                override fun onFailure(call: Call<Restaurants>, t: Throwable) {
                    // Show error message
                    errorMessageView.text = "${t.message}"
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