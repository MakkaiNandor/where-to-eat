package com.example.androidproject.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidproject.R
import com.example.androidproject.fragment.LoginFragment

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        supportFragmentManager.beginTransaction().replace(R.id.current_fragment, LoginFragment.newInstance()).commit()

        /*val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()*/
    }
}