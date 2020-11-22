package com.example.androidproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidproject.model.Restaurant

class MainActivity : AppCompatActivity() {
    companion object{
        private val item = Restaurant(
            107257,
            "Las Tablas Colombian Steak House",
            "2942 N Lincoln Ave",
            "Chicago",
            "IL",
            "Chicago / Illinois",
            "60657",
            "US",
            "7738712414",
            41.935137,
            -87.662815,
            2,
            "http://www.opentable.com/single.aspx?rid=107257",
            "http://mobile.opentable.com/opentable/?restId=107257",
            "https://www.opentable.com/img/restimages/107257.jpg"
        )
        var itemList : List<Restaurant> = listOf(item, item, item, item, item, item, item, item, item, item, item, item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}