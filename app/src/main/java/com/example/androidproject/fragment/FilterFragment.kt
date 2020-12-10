package com.example.androidproject.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.example.androidproject.R
import com.example.androidproject.api.RetrofitInstance.api
import com.example.androidproject.api.model.Cities
import retrofit2.Call
import retrofit2.Response

class FilterFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_filter, container, false)

        // Get spinner of cities
        val citySpinner = root.findViewById<Spinner>(R.id.city_spinner)

        // Click event listener for 'Save' button, this will redirect to loading fragment
        root.findViewById<Button>(R.id.save_button).setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                LoadingFragment()
            )?.commit()
        }

        // Get cities from API
        /*api.getCities().enqueue(object: retrofit2.Callback<Cities>{
            override fun onResponse(call: Call<Cities>, response: Response<Cities>) {
                if(response.isSuccessful) {
                    // Set up list of cities for spinner
                    citySpinner.adapter = ArrayAdapter<String>(
                        requireContext(),
                        R.layout.support_simple_spinner_dropdown_item,
                        response.body()!!.cities)
                }
                else{
                    // Log the error
                    Log.d("Response", "Failed: ${response.code()}, ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Cities>, t: Throwable) {
                // Log the error
                Log.d("Response", "onFailure")
                Log.d("Response", "Error: ${t.message}")
                Log.d("Response", "Error: $t")
            }

        })*/

        return root
    }

}