package com.example.androidproject.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.R
import com.example.androidproject.RestaurantAdapter
import com.example.androidproject.activity.MainActivity
import com.example.androidproject.api.model.Restaurant
import com.example.androidproject.database.DbViewModel
import com.example.androidproject.database.DbViewModelFactory

class ProfileFragment : Fragment(), RestaurantAdapter.OnItemClickListener {

    private lateinit var dbViewModel: DbViewModel
    private var editMode: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        dbViewModel = ViewModelProvider(this, DbViewModelFactory(requireActivity().application)).get(DbViewModel::class.java)

        val personalDataContainer: ConstraintLayout = root.findViewById(R.id.personal_data_section)

        // Display user's personal data
        setUpPersonalData(personalDataContainer)

        personalDataContainer.findViewById<Button>(R.id.edit_button).setOnClickListener {
            val btn: Button = it as Button
            if(editMode){
                if(savePersonalData(personalDataContainer)) {
                    if(MainActivity.loggedInUser != null){
                        dbViewModel.updateUser(MainActivity.loggedInUser!!)
                    }
                    setUpPersonalData(personalDataContainer)
                    btn.text = resources.getText(R.string.edit_button_text)
                    editMode = !editMode
                }
            }
            else{
                changeToEditMode(personalDataContainer)
                btn.text = resources.getText(R.string.save_button_text)
                editMode = !editMode
            }
        }

        // Display user's favorite restaurants
        val favoriteList = root.findViewById<RecyclerView>(R.id.favorites_list)
        val adapter = RestaurantAdapter(requireContext(), this)
        val userFavorites = dbViewModel.getUserFavorites()
        adapter.setRestaurants(userFavorites)
        adapter.setUserFavorites(userFavorites.map { it.id })
        favoriteList.adapter = adapter
        favoriteList.layoutManager = LinearLayoutManager(requireContext())
        favoriteList.setHasFixedSize(true)

        return root
    }

    private fun setUpPersonalData(parent: View){
        parent.findViewById<TextView>(R.id.name_value).text = MainActivity.loggedInUser?.name
        parent.findViewById<TextView>(R.id.email_value).text = MainActivity.loggedInUser?.email
        parent.findViewById<TextView>(R.id.address_value).text = MainActivity.loggedInUser?.address
        parent.findViewById<TextView>(R.id.phone_value).text = MainActivity.loggedInUser?.phone
    }

    private fun changeToEditMode(parent: View){
        val nameText: TextView = parent.findViewById(R.id.name_value)
        val addressText: TextView = parent.findViewById(R.id.address_value)
        val phoneText: TextView = parent.findViewById(R.id.phone_value)
        val nameInput: TextView = parent.findViewById(R.id.name_input)
        val addressInput: TextView = parent.findViewById(R.id.address_input)
        val phoneInput: TextView = parent.findViewById(R.id.phone_input)
        nameInput.text = nameText.text
        addressInput.text = addressText.text
        phoneInput.text = phoneText.text
        nameText.visibility = View.GONE
        addressText.visibility = View.GONE
        phoneText.visibility = View.GONE
        nameInput.visibility = View.VISIBLE
        addressInput.visibility = View.VISIBLE
        phoneInput.visibility = View.VISIBLE
    }

    private fun savePersonalData(parent: View): Boolean {
        val nameText: TextView = parent.findViewById(R.id.name_value)
        val addressText: TextView = parent.findViewById(R.id.address_value)
        val phoneText: TextView = parent.findViewById(R.id.phone_value)
        val nameInput: TextView = parent.findViewById(R.id.name_input)
        val addressInput: TextView = parent.findViewById(R.id.address_input)
        val phoneInput: TextView = parent.findViewById(R.id.phone_input)
        var error = false
        if(nameInput.text.isBlank()){
            nameInput.error = "Field is empty"
            error = true
        }
        if(addressInput.text.isBlank()){
            addressInput.error = "Field is empty"
            error = true
        }
        if(phoneInput.text.isBlank()){
            phoneInput.error = "Field is empty"
            error = true
        }
        if(error) return false
        nameInput.visibility = View.GONE
        addressInput.visibility = View.GONE
        phoneInput.visibility = View.GONE
        nameText.visibility = View.VISIBLE
        addressText.visibility = View.VISIBLE
        phoneText.visibility = View.VISIBLE
        if(MainActivity.loggedInUser != null) {
            MainActivity.loggedInUser!!.name = nameInput.text.toString().trim()
            MainActivity.loggedInUser!!.address = addressInput.text.toString().trim()
            MainActivity.loggedInUser!!.phone = phoneInput.text.toString().trim()
        }
        return true
    }

    override fun onItemClick(position: Int) {

    }

    override fun onFavIconClick(item: Restaurant, favorite: Boolean) {
        if(favorite){
            dbViewModel.addUserFavorite(item)
        }
        else{
            dbViewModel.removeUserFavorite(item)
        }
    }

}