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
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.R
import com.example.androidproject.activity.ListType
import com.example.androidproject.activity.MainActivity
import com.example.androidproject.api.ApiRepository
import com.example.androidproject.api.DataViewModel
import com.example.androidproject.api.DataViewModelFactory
import kotlinx.android.synthetic.main.fragment_filter.*

class FilterFragment : Fragment() {

    private lateinit var viewModel: DataViewModel
    private lateinit var cityAdapter: ArrayAdapter<String>
    private lateinit var listTypeRadioGroup: RadioGroup
    private lateinit var citySpinner: Spinner
    private lateinit var priceRadioGroup: RadioGroup

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_filter, container, false)

        // Create view model
        val repository = ApiRepository()
        viewModel = ViewModelProvider(this, DataViewModelFactory(repository)).get(DataViewModel::class.java)

        // Get spinner of cities and set up adapter
        citySpinner = root.findViewById<Spinner>(R.id.city_spinner)
        cityAdapter = ArrayAdapter<String>(requireContext(), R.layout.support_simple_spinner_dropdown_item, mutableListOf("All"))
        citySpinner.adapter = cityAdapter

        // Get cites for spinner, add them to adapter and set default value
        viewModel.getCities()
        viewModel.citiesResponse.observe(viewLifecycleOwner, Observer { response ->
            if(response.isSuccessful){
                cityAdapter.addAll(response.body()!!.cities)
                if(!MainActivity.filters["city"].isNullOrEmpty()){
                    citySpinner.setSelection(cityAdapter.getPosition(MainActivity.filters["city"]))
                }
                Log.d("DEBUG", "Cities successfully arrived")
            }
            else{
                Log.d("DEBUG", "Error ${response.code()}: ${response.errorBody()}")
            }
        })

        // Get radio group of data list type and set default value
        listTypeRadioGroup = root.findViewById<RadioGroup>(R.id.listed_data_radio_group)
        val prevSelectedListType: Int = when(MainActivity.listType){
            ListType.ALL -> R.id.listed_data_all
            ListType.FAVORITES -> R.id.listed_data_favorites
            ListType.WITHOUT_FAVORITES -> R.id.listed_data_without_favorites
        }
        listTypeRadioGroup.check(prevSelectedListType)

        // Get radio group of price and set default value
        priceRadioGroup = root.findViewById<RadioGroup>(R.id.price_radio_group)
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
            MainActivity.listType = when(listTypeRadioGroup.checkedRadioButtonId){
                R.id.listed_data_all -> ListType.ALL
                R.id.listed_data_favorites -> ListType.FAVORITES
                else -> ListType.WITHOUT_FAVORITES
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

            activity?.supportFragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container_main,
                LoadingFragment()
            )?.commit()
        }

        // Click event listener for 'Back' button, this will redirect back to list fragment
        root.findViewById<Button>(R.id.back_button).setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container_main,
                LoadingFragment()
            )?.commit()
        }

        return root
    }

}