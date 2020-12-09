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
import com.example.androidproject.model.Restaurants
import retrofit2.Call
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 * Use the [LoadingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoadingFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_loading, container, false)
        val errorMessageView = root.findViewById<TextView>(R.id.error)
        val connection = isNetworkAvailable()

        if(connection){
            api.getRestaurants(MainActivity.filters).enqueue(object : retrofit2.Callback<Restaurants>{
                override fun onResponse(call: Call<Restaurants>, response: Response<Restaurants>) {
                    if(response.isSuccessful) {
                        Log.d("Response", "onResponse")
                        Log.d("Response", "Cities: ${response.body()!!.total_entries}")
                        MainActivity.restaurants = response.body()!!.restaurants
                        fragmentManager?.beginTransaction()?.replace(
                                R.id.fragment_container,
                                ListFragment.newInstance()
                        )?.commit()
                    }
                    else{
                        Log.d("Response", "Failed: ${response.code()}, ${response.message()}")
                        val errorMsg = "Error ${response.code()}: ${response.message()}"
                        errorMessageView.text = errorMsg
                    }
                }

                override fun onFailure(call: Call<Restaurants>, t: Throwable) {
                    Log.d("Response", "onFailure")
                    Log.d("Response", "Error: ${t.message}")
                    Log.d("Response", "Error: $t")
                    errorMessageView.text = "${t.message}"
                }
            })
        }
        else{
            val errMsg = "Not internet connection, please check it."
            errorMessageView.text = errMsg
        }

        return root
    }

    fun isNetworkAvailable() : Boolean {
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment LoadingFragment.
         */
        @JvmStatic
        fun newInstance() = LoadingFragment()
    }
}