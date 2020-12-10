package com.example.androidproject.fragment

import android.os.Bundle
import android.provider.ContactsContract
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.R
import com.example.androidproject.RestaurantAdapter
import com.example.androidproject.activity.MainActivity
import com.example.androidproject.api.ApiRepository
import com.example.androidproject.api.DataViewModel
import com.example.androidproject.api.DataViewModelFactory
import com.example.androidproject.data.UserViewModel

class ListFragment : Fragment() {

    //private lateinit var viewModel: DataViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_list, container, false)

        /*val repository =  ApiRepository()
        viewModel = ViewModelProvider(this, DataViewModelFactory(repository)).get(DataViewModel::class.java)*/

        // Set up recycler view
        val recyclerView = root.findViewById<RecyclerView>(R.id.rest_list)
        val adapter = RestaurantAdapter(requireContext())
        adapter.setList(MainActivity.restaurants)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enable options menu in action bar
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Own action bar menu
        inflater.inflate(R.menu.list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Actions for option items
        return when (item.itemId) {
            R.id.filter_item -> {
                fragmentManager?.beginTransaction()?.replace(
                    R.id.fragment_container,
                    FilterFragment()
                )?.addToBackStack(null)?.commit()
                true
            }
            else -> false
        }
    }

}