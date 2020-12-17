package com.example.androidproject.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.R
import com.example.androidproject.RestaurantAdapter
import com.example.androidproject.api.model.Restaurant
import com.example.androidproject.database.DbViewModel
import com.example.androidproject.database.DbViewModelFactory

class ProfileFragment : Fragment(), RestaurantAdapter.OnItemClickListener {

    lateinit var dbViewModel: DbViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        dbViewModel = ViewModelProvider(requireActivity(), DbViewModelFactory(activity?.application!!)).get(DbViewModel::class.java)

        val favoriteList = root.findViewById<RecyclerView>(R.id.favorites_list)
        val adapter = RestaurantAdapter(requireContext(), this)
        adapter.setList(dbViewModel.getUserFavorites())
        favoriteList.adapter = adapter
        favoriteList.layoutManager = LinearLayoutManager(requireContext())
        favoriteList.setHasFixedSize(true)

        return root
    }

    override fun onItemClick(position: Int) {

    }

    override fun onFavIconClick(item: Restaurant, favorite: Boolean) {

    }

}