package com.example.androidproject.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.fragment.LoadingFragment
import com.example.androidproject.R
import com.example.androidproject.api.model.Restaurant
import com.example.androidproject.database.DbViewModel
import com.example.androidproject.database.DbViewModelFactory
import com.example.androidproject.database.entity.User
import com.example.androidproject.fragment.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

enum class DisplayType {
    ALL, FAVORITES, WITHOUT_FAVORITES
}

class MainActivity : AppCompatActivity() {

    companion object {
        var filters: Map<String, String> = mapOf()
        var displayType: DisplayType = DisplayType.ALL
        var loggedInUser: User? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("DEBUG", "${loggedInUser?.name}")

        // Click event handler for bottom navigation bar
        findViewById<BottomNavigationView>(R.id.bottom_nav).setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.list_item -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container_main, LoadingFragment()).commit()
                    true
                }
                R.id.profile_item -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container_main, ProfileFragment()).commit()
                    true
                }
                else -> false
            }
        }

        // Start the loading fragment
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container_main, LoadingFragment()).commit()
    }
}