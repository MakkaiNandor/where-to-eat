package com.example.androidproject.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.androidproject.fragment.LoadingFragment
import com.example.androidproject.R
import com.example.androidproject.model.Restaurant
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
}