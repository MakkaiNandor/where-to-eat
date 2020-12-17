package com.example.androidproject.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.R
import com.example.androidproject.activity.DisplayType
import com.example.androidproject.activity.MainActivity
import com.example.androidproject.api.ApiRepository
import com.example.androidproject.api.ApiViewModel
import com.example.androidproject.api.ApiViewModelFactory

class FilterFragment : Fragment() {

    private lateinit var apiViewModel: ApiViewModel
    private lateinit var cityAdapter: ArrayAdapter<String>
    private lateinit var displayTypeRadioGroup: RadioGroup
    private lateinit var citySpinner: Spinner
    private lateinit var priceRadioGroup: RadioGroup

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_filter, container, false)

        apiViewModel = ViewModelProvider(this, ApiViewModelFactory(ApiRepository())).get(ApiViewModel::class.java)

        // Create spinner of cities and add data to it
        citySpinner = root.findViewById(R.id.city_spinner)
        cityAdapter = ArrayAdapter<String>(requireContext(), R.layout.support_simple_spinner_dropdown_item, mutableListOf("All"))
        citySpinner.adapter = cityAdapter
        apiViewModel.getCities()
        apiViewModel.citiesResponse.observe(viewLifecycleOwner, Observer { response ->
            if(response.isSuccessful){
                cityAdapter.addAll(response.body()!!.cities)
                if(!MainActivity.filters["city"].isNullOrEmpty()){
                    citySpinner.setSelection(cityAdapter.getPosition(MainActivity.filters["city"]))
                }
                Log.d("DEBUG", "Cities successfully received")
            }
            else{
                Log.d("DEBUG", "Error ${response.code()}: ${response.errorBody()}")
            }
        })

        // Get radio group of data list type and set default value
        displayTypeRadioGroup = root.findViewById(R.id.listed_data_radio_group)
        val prevSelectedDisplayType: Int = when(MainActivity.displayType){
            DisplayType.ALL -> R.id.listed_data_all
            DisplayType.FAVORITES -> R.id.listed_data_favorites
            DisplayType.WITHOUT_FAVORITES -> R.id.listed_data_without_favorites
        }
        displayTypeRadioGroup.check(prevSelectedDisplayType)

        // Get radio group of price and set default value
        priceRadioGroup = root.findViewById(R.id.price_radio_group)
        val prevSelectedPrice: Int = when(MainActivity.filters["price"]){
            "1" -> R.id.price_1
            "2" -> R.id.price_2
            "3" -> R.id.price_2
            "4" -> R.id.price_3
            else -> R.id.price_all
        }
        priceRadioGroup.check(prevSelectedPrice)

        // Click event listener for 'Save' button, this will redirect to loading fragment
        root.findViewById<Button>(R.id.save_button).setOnClickListener {
            // Get selected type of list
            MainActivity.displayType = when(displayTypeRadioGroup.checkedRadioButtonId){
                R.id.listed_data_all -> DisplayType.ALL
                R.id.listed_data_favorites -> DisplayType.FAVORITES
                else -> DisplayType.WITHOUT_FAVORITES
            }

            // Get selected city
            if(citySpinner.selectedItemPosition == 0){
                MainActivity.filters = MainActivity.filters.minus("city")
            }
            else {
                MainActivity.filters = MainActivity.filters.plus("city" to citySpinner.selectedItem.toString())
            }

            // Get selected price
            val selectedPrice: String? = when(priceRadioGroup.checkedRadioButtonId){
                R.id.price_1 -> "1"
                R.id.price_2 -> "2"
                R.id.price_3 -> "3"
                R.id.price_4 -> "4"
                else -> null
            }
            if(selectedPrice == null){
                MainActivity.filters = MainActivity.filters.minus("price")
            }
            else{
                MainActivity.filters = MainActivity.filters.plus("price" to selectedPrice)
            }

            requireActivity().supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container_main,
                LoadingFragment()
            ).commit()
        }

        // Click event listener for 'Back' button, this will redirect back to list fragment
        root.findViewById<Button>(R.id.back_button).setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container_main,
                LoadingFragment()
            ).commit()
        }

        return root
    }

}