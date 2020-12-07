package com.example.androidproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.RetrofitInstance.api
import com.example.androidproject.model.Restaurants
import retrofit2.Call
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_list, container, false)

        val recyclerView = root.findViewById<RecyclerView>(R.id.rest_list)
        recyclerView.adapter = RestaurantAdapter(this.context!!, listOf())
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.setHasFixedSize(true)

        val filters = if (MainActivity.searchFilters.isEmpty()) MainActivity.baseSearchFilter else MainActivity.searchFilters
        api.getRestaurants(filters).enqueue(object: retrofit2.Callback<Restaurants> {
            override fun onResponse(call: Call<Restaurants>, response: Response<Restaurants>) {
                if(response.isSuccessful) {
                    Log.d("Response", "onResponse")
                    Log.d("Response", "Restaurants: ${response.body()!!.total_entries}")
                    recyclerView.adapter = RestaurantAdapter(requireContext(), response.body()!!.restaurants)
                }
                else{
                    Log.d("Response", "Failed: ${response.code()}, ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Restaurants>, t: Throwable) {
                Log.d("Response", "onFailure")
                Log.d("Response", "Error: ${t.message}")
                Log.d("Response", "Error: $t")
            }
        })
        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ListFragment.
         */
        @JvmStatic
        fun newInstance() = ListFragment()
    }
}