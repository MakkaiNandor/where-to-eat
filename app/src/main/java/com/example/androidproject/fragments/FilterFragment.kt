package com.example.androidproject.fragments

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
import com.example.androidproject.RetrofitInstance.api
import com.example.androidproject.models.Cities
import retrofit2.Call
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 * Use the [FilterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FilterFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_filter, container, false)

        val citySpinner = root.findViewById<Spinner>(R.id.city_spinner)

        root.findViewById<Button>(R.id.save_button).setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                LoadingFragment.newInstance()
            )?.commit()
        }

        api.getCities().enqueue(object: retrofit2.Callback<Cities>{
            override fun onResponse(call: Call<Cities>, response: Response<Cities>) {
                if(response.isSuccessful) {
                    Log.d("Response", "onResponse")
                    Log.d("Response", "Cities: ${response.body()!!.count}")
                    citySpinner.adapter = ArrayAdapter<String>(requireContext(),
                        R.layout.support_simple_spinner_dropdown_item, response.body()!!.cities)
                }
                else{
                    Log.d("Response", "Failed: ${response.code()}, ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Cities>, t: Throwable) {
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
         * @return A new instance of fragment FilterFragment.
         */
        @JvmStatic
        fun newInstance() = FilterFragment()
    }
}