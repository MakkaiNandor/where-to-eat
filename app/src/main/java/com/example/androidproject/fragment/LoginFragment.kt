package com.example.androidproject.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.androidproject.R
import com.example.androidproject.activity.MainActivity

class LoginFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_login, container, false)

        root.findViewById<Button>(R.id.login_button).setOnClickListener {
            startActivity(Intent(requireContext(), MainActivity::class.java))
            activity?.finish()
        }

        return root
    }

}