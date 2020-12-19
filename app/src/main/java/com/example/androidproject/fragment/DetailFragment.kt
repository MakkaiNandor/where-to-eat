package com.example.androidproject.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.adapter.ImageAdapter
import com.example.androidproject.R
import com.example.androidproject.api.model.Restaurant
import com.example.androidproject.database.DbViewModel
import com.example.androidproject.database.DbViewModelFactory
import kotlinx.coroutines.coroutineScope
import java.io.ByteArrayOutputStream
import java.lang.Exception

class DetailFragment(
        private val restaurant: Restaurant,
        private var favorite: Boolean
) : Fragment(), ImageAdapter.OnItemLongClickListener {

    companion object {
        private const val IMAGE_PICK_CODE = 1
        private const val GALLERY_PERMISSION_CODE = 2
        private const val CAMERA_CODE = 3
        private const val CAMERA_PERMISSION_CODE = 4
        private const val PHONE_PERMISSION_CODE = 5
    }

    private lateinit var dbViewModel: DbViewModel
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var imageList: RecyclerView

    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_detail, container, false)

        dbViewModel = ViewModelProvider(this, DbViewModelFactory(requireActivity().application)).get(DbViewModel::class.java)

        displayRestaurantData(root)

        // Set up nested Google Maps
        // requireActivity().supportFragmentManager.beginTransaction().replace(R.id.google_map_container, MapFragment(restaurant.lat, restaurant.lng)).commit()

        // Show restaurant on Google Maps
        root.findViewById<ImageView>(R.id.google_maps_btn).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:${restaurant.lat},${restaurant.lng}"))
            intent.setPackage("com.google.android.apps.maps")
            if(intent.resolveActivity(requireActivity().packageManager) != null){
                startActivity(intent)
            }
            else{
                Toast.makeText(requireContext(), "You need Google Maps!", Toast.LENGTH_LONG).show()
            }
        }

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

        // Call the restaurant
        root.findViewById<Button>(R.id.call_btn).setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED){
                    val permissions = arrayOf(android.Manifest.permission.CALL_PHONE)
                    requestPermissions(permissions, PHONE_PERMISSION_CODE)
                }
                else{
                    if(!callTheRestaurant()){
                        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                if(!callTheRestaurant()){
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Pick image from external storage
        root.findViewById<ImageView>(R.id.gallery_btn).setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, GALLERY_PERMISSION_CODE)
                }
                else{
                    if(!pickImageFromGallery()) {
                        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                if(!pickImageFromGallery()){
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                }
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
                    if(!createImageWithCamera()){
                        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                if(!createImageWithCamera()){
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Set up images
        imageAdapter = ImageAdapter(requireContext(), dbViewModel.getUserImagesByRestaurant(restaurant.id).map {
            BitmapFactory.decodeByteArray(it, 0, it.size)
        } as MutableList<Bitmap>, this)
        imageList = root.findViewById(R.id.image_list)
        imageList.adapter = imageAdapter
        imageList.layoutManager = GridLayoutManager(requireContext(), 2)
        imageList.setHasFixedSize(true)

        return root
    }

    private fun callTheRestaurant(): Boolean {
        return try {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:${restaurant.phone}"))
            startActivity(intent)
            true
        } catch (e: Exception){
            e.printStackTrace()
            false
        }
    }

    private fun pickImageFromGallery(): Boolean {
        return try {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK_CODE)
            true
        } catch(e: Exception){
            e.printStackTrace()
            false
        }
    }

    private fun createImageWithCamera(): Boolean {
        return try {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA_CODE)
            true
        } catch (e: Exception){
            e.printStackTrace()
            false
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            when(requestCode){
                PHONE_PERMISSION_CODE -> if(!callTheRestaurant()) Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                GALLERY_PERMISSION_CODE -> if(!pickImageFromGallery()) Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
                CAMERA_PERMISSION_CODE -> if(!createImageWithCamera()) Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(requireContext(), "Permission denied!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK){
            Log.d("DEBUG", "Image from gallery")
            val imgURI = data?.data ?: return
            val imgBitmap = BitmapFactory.decodeStream(requireActivity().contentResolver.openInputStream(imgURI))
            imageAdapter.addImage(imgBitmap)
            val imgStream = ByteArrayOutputStream()
            imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, imgStream)
            Log.d("DEBUG", imgStream.toByteArray().size.toString())
            dbViewModel.addImageToRestaurant(restaurant, imgStream.toByteArray())
            Log.d("DEBUG", imgBitmap.toString())
        }
        else if(requestCode == CAMERA_CODE && resultCode == Activity.RESULT_OK){
            Log.d("DEBUG", "Image from camera")
            val imgBitmap = data?.extras?.get("data") as Bitmap
            imageAdapter.addImage(imgBitmap)
            val imgStream = ByteArrayOutputStream()
            imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, imgStream)
            Log.d("DEBUG", imgStream.toByteArray().size.toString())
            dbViewModel.addImageToRestaurant(restaurant, imgStream.toByteArray())
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

    /**
     * Delete one image
     */
    override fun onItemLongClick(item: Bitmap) {
        val imgStream = ByteArrayOutputStream()
        item.compress(Bitmap.CompressFormat.PNG, 100, imgStream)
        dbViewModel.removeImageFromRestaurant(restaurant.id, imgStream.toByteArray())
        Toast.makeText(requireContext(), "Image deleted", Toast.LENGTH_SHORT).show()
    }

}