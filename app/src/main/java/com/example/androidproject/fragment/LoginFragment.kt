package com.example.androidproject.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.R
import com.example.androidproject.activity.MainActivity
import com.example.androidproject.database.*

class LoginFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var errorMessageView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_login, container, false)

        userViewModel = ViewModelProvider(this, UserViewModelFactory(activity?.application!!)).get(UserViewModel::class.java)

        // Error message
        errorMessageView = root.findViewById<TextView>(R.id.login_error)
        errorMessageView.text = ""

        // On 'Login' button clicked
        root.findViewById<Button>(R.id.login_button).setOnClickListener {
            val emailInput = root.findViewById<EditText>(R.id.email_input)
            val passwordInput = root.findViewById<EditText>(R.id.password_input)
            onLoginClicked(emailInput, passwordInput)
        }

        // On 'Register' button clicked
        root.findViewById<Button>(R.id.goto_register_button).setOnClickListener {
            onRegisterClicked()
        }

        return root
    }

    /**
     * Check the user's login data
     */
    private fun onLoginClicked(email: EditText, password: EditText){
        if(checkEmail(email) && checkPassword(password)) {
            if (userViewModel.checkUserForLogin(email.text.toString().trim(), password.text.toString().trim()) > 0) {
                userViewModel.setupLoggedInUser(email.text.toString().trim())
                redirectToMainActivity()
            } else {
                val errMsg = "User doesn't exist!"
                errorMessageView.text = errMsg
            }
        }
    }

    private fun checkEmail(email: EditText): Boolean {
        val emailVal = email.text.toString().trim()
        if(emailVal.isEmpty()){
            email.error = "Field is empty"
            return false
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emailVal).matches()){
            email.error = "Invalid email address"
            return false
        }
        return true
    }

    private fun checkPassword(password: EditText): Boolean {
        val passwordVal = password.text.toString().trim()
        if(passwordVal.isEmpty()){
            password.error = "Field is empty"
            return false
        }
        if(passwordVal.length < 6){
            password.error = "Too short password"
            return false
        }
        if(passwordVal.matches(".*\\s.*".toRegex())){
            password.error = "Contains whitespaces"
            return false
        }
        return true
    }

    /**
     * Redirect user to the registration screen
     */
    private fun onRegisterClicked(){
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment_container_start, RegisterFragment())?.addToBackStack(null)?.commit()
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