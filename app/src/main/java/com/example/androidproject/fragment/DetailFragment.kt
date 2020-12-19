package com.example.androidproject.fragment

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.R
import com.example.androidproject.api.model.Restaurant
import com.example.androidproject.database.DbViewModel
import com.example.androidproject.database.DbViewModelFactory
import java.io.ByteArrayOutputStream

class DetailFragment(
        private val restaurant: Restaurant,
        private var favorite: Boolean
) : Fragment() {

    companion object {
        private const val IMAGE_PICK_CODE = 1000
        private const val GALLERY_PERMISSION_CODE = 1001
        private const val CAMERA_CODE = 2000
        private const val CAMERA_PERMISSION_CODE = 2001
    }

    private lateinit var dbViewModel: DbViewModel
    private lateinit var image: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_detail, container, false)

        dbViewModel = ViewModelProvider(this, DbViewModelFactory(requireActivity().application)).get(DbViewModel::class.java)

        displayRestaurantData(root)

        // Add/remove restaurant to/from favorites
        val favoriteBtn: ImageView = root.findViewById(R.id.favorite_btn)
        favoriteBtn.setImageResource(if(favorite) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star_big_off)
        favoriteBtn.setOnClickListener {
            val favIcon = it as ImageView
            if(favorite){
                favIcon.setImageResource(android.R.drawable.btn_star_big_off)
                dbViewModel.removeUserFavorite(restaurant.id)
            }
            else{
                favIcon.setImageResource(android.R.drawable.btn_star_big_on)
                dbViewModel.addUserFavorite(restaurant)
            }
            favorite = !favorite
        }

        image = root.findViewById(R.id.restaurant_image)

        // Pick image from external storage
        root.findViewById<ImageView>(R.id.gallery_btn).setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, GALLERY_PERMISSION_CODE)
                }
                else{
                    pickImageFromGallery()
                }
            }
            else{
                pickImageFromGallery()
            }
        }

        // Create image with camera
        root.findViewById<ImageView>(R.id.camera_btn).setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
                    val permissions = arrayOf(android.Manifest.permission.CAMERA)
                    requestPermissions(permissions, CAMERA_PERMISSION_CODE)
                }
                else{
                    createImageWithCamera()
                }
            }
            else{
                createImageWithCamera()
            }
        }

        return root
    }

    private fun pickImageFromGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    private fun createImageWithCamera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(requestCode == GALLERY_PERMISSION_CODE) {
                pickImageFromGallery()
            }
            else if(requestCode == CAMERA_PERMISSION_CODE){
                createImageWithCamera()
            }
        }
        else{
            Toast.makeText(requireContext(), "Permission denied!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK){
            val imgURI = data?.data ?: return
            val imgBitmap = BitmapFactory.decodeStream(requireActivity().contentResolver.openInputStream(imgURI))
            image.setImageBitmap(imgBitmap)
            Log.d("DEBUG", imgBitmap.toString())
        }
        else if(requestCode == CAMERA_CODE && resultCode == Activity.RESULT_OK){
            val imgBitmap = data?.extras?.get("data") as Bitmap
            image.setImageBitmap(imgBitmap)
            val imgStream = ByteArrayOutputStream()
            imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, imgStream)
            val result = imgStream.toByteArray()
            Log.d("DEBUG", result.size.toString())
            Log.d("DEBUG", imgBitmap.toString())
        }
    }

    private fun displayRestaurantData(parent: View){
        parent.findViewById<TextView>(R.id.restaurant_name).text = restaurant.name
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