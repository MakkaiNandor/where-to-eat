package com.example.androidproject.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.R
import com.example.androidproject.activity.MainActivity
import com.example.androidproject.data.User
import com.example.androidproject.data.UserDatabase
import com.example.androidproject.data.UserRepository
import com.example.androidproject.data.UserViewModel
import kotlinx.coroutines.*

class LoginFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_login, container, false)

        // Get UserViewModel
        val viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(activity?.application!!)).get(UserViewModel::class.java)

        // On 'Login' button clicked
        root.findViewById<Button>(R.id.login_button).setOnClickListener {
            // Check the inputs
            val emailInput = root.findViewById<EditText>(R.id.email_input).text.toString().trim()
            val passwordInput = root.findViewById<EditText>(R.id.password_input).text.toString().trim()
            val errorMessage = root.findViewById<TextView>(R.id.error)
            if(emailInput.isEmpty() || passwordInput.isEmpty()){
                val errMsg = "Email and/or password field is empty"
                errorMessage.text = errMsg
            }
            else if(viewModel.checkUser(emailInput, passwordInput)) {
                redirectToMainActivity()
            }
            else{
                val errMsg = "User doesn't exist!"
                errorMessage.text = errMsg
            }
        }

        return root
    }

    /**
     * Start the Main Activity.
     */
    private fun redirectToMainActivity(){
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

}