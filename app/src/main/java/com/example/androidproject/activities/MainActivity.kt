package com.example.androidproject.activities

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.androidproject.fragments.LoadingFragment
import com.example.androidproject.R
import com.example.androidproject.models.Restaurant
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    companion object {
        var filters : Map<String,String> = mapOf("city" to "Chicago")
        val baseSearchFilter : Map<String, String> = mapOf("city" to "Chicago")
        var searchFilters : Map<String, String> = mapOf()
        var restaurants : List<Restaurant> = listOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val message = if(isNetworkAvailable()) "Connection OK" else "No internet connection"
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, LoadingFragment.newInstance()).commit()

        findViewById<BottomNavigationView>(R.id.bottom_nav).setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.list_item -> {
                    Toast.makeText(this, "List clicked!", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.profile_item -> {
                    Toast.makeText(this, "Profile clicked!", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }
    }

    private fun isNetworkAvailable() : Boolean {
        val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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