package com.example.androidproject

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidproject.model.Restaurant
import com.example.androidproject.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository) : ViewModel() {

    val myResponse: MutableLiveData<Response<Restaurant>> = MutableLiveData()

    fun getRestaurant(id: Int) {
        viewModelScope.launch {
            val response = repository.getRestaurant(id)
            myResponse.value = response
        }
    }

}