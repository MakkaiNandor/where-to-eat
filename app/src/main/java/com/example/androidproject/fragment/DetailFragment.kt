package com.example.androidproject.fragment

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Image
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.example.androidproject.R
import com.example.androidproject.api.model.Restaurant
import java.util.jar.Manifest

class DetailFragment(private val restaurant: Restaurant) : Fragment() {

    companion object {
        private const val IMAGE_PICK_CODE = 1000
        private const val PERMISSION_CODE = 1001
    }

    private lateinit var image: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_detail, container, false)

        displayRestaurantData(root)

        image = root.findViewById(R.id.restaurant_image)

        root.findViewById<ImageView>(R.id.gallery_btn).setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE)
                }
                else{
                    pickImageFromGallery()
                }
            }
            else{
                pickImageFromGallery()
            }
        }

        return root
    }

    private fun pickImageFromGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE ->
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery()
                }
                else{
                    Toast.makeText(requireContext(), "Permission denied!", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            image.setImageURI(data?.data)
        }
    }

    private fun displayRestaurantData(parent: View){
        parent.findViewById<TextView>(R.id.restaurant_name).text = restaurant.name
        Glide.with(requireContext()).load(restaurant.image_url).into(parent.findViewById(R.id.restaurant_image))
        parent.findViewById<TextView>(R.id.price_value).text = restaurant.price.toString()
        parent.findViewById<TextView>(R.id.country_value).text = restaurant.country
        parent.findViewById<TextView>(R.id.state_value).text = restaurant.state
        parent.findViewById<TextView>(R.id.area_value).text = restaurant.area
        parent.findViewById<TextView>(R.id.city_value).text = restaurant.city
        parent.findViewById<TextView>(R.id.address_value).text = restaurant.address
        parent.findViewById<TextView>(R.id.postal_code_value).text = restaurant.postal_code
        parent.findViewById<TextView>(R.id.phone_value).text = restaurant.phone
    }

}