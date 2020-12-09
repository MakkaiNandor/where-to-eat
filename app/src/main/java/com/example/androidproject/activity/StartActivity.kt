package com.example.androidproject.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidproject.R
import com.example.androidproject.fragment.LoginFragment

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_AndroidProject)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        // Start the login fragment
        supportFragmentManager.beginTransaction().replace(R.id.current_fragment, LoginFragment()).commit()

        /*val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()*/
    }
}