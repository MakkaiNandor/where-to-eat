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
import androidx.lifecycle.ViewModelProvider
import com.example.androidproject.R
import com.example.androidproject.activity.MainActivity
import com.example.androidproject.database.entity.User
import com.example.androidproject.database.DbViewModel

class RegisterFragment : Fragment() {

    private lateinit var dbViewModel: DbViewModel
    private lateinit var errorMessageView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_register, container, false)

        dbViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory(activity?.application!!)).get(DbViewModel::class.java)

        // Error message
        errorMessageView = root.findViewById<TextView>(R.id.register_error)
        errorMessageView.text = ""

        // On 'Register' button clicked
        root.findViewById<Button>(R.id.register_button).setOnClickListener {
            val name = root.findViewById<EditText>(R.id.name_input)
            val email = root.findViewById<EditText>(R.id.email_input)
            val address = root.findViewById<EditText>(R.id.address_input)
            val phone = root.findViewById<EditText>(R.id.phone_input)
            val password = root.findViewById<EditText>(R.id.password_input)
            val confirmation = root.findViewById<EditText>(R.id.confirm_input)

            onRegisterClicked(name, email, address, phone, password, confirmation)
        }

        return root
    }

    /**
     * Check the user's registration data
     */
    private fun onRegisterClicked(name: EditText, email: EditText, address: EditText, phone: EditText, password: EditText, confirmation: EditText){
        if(checkEmail(email) && checkPasswords(password, confirmation) && checkOthers(name, address, phone)){
            if(dbViewModel.checkUserForRegister(email.text.toString().trim()) == 0){
                val newUser = User(
                        name.text.toString().trim(),
                        email.text.toString().trim(),
                        address.text.toString().trim(),
                        phone.text.toString().trim(),
                        password.text.toString().trim()
                )
                dbViewModel.addUser(newUser)
                redirectToMainActivity(newUser.email)
            }
            else{
                val errMsg = "One user already exists with the same email address!"
                errorMessageView.text = errMsg;
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

    private fun checkPasswords(password: EditText, confirmation: EditText): Boolean {
        val passwordVal = password.text.toString().trim()
        val confirmationVal = confirmation.text.toString().trim()
        if(passwordVal.isEmpty()){
            password.error = "Field is empty"
            return false
        }
        if(confirmationVal.isEmpty()){
            confirmation.error = "Field is empty"
            return false
        }
        if(passwordVal.length < 6){
            password.error = "Too short password"
            return false
        }
        if(confirmationVal.length < 6){
            confirmation.error = "Too short password"
            return false
        }
        if(passwordVal.matches(".*\\s.*".toRegex())){
            password.error = "Contains whitespaces"
            return false
        }
        if(confirmationVal.matches(".*\\s.*".toRegex())){
            password.error = "Contains whitespaces"
            return false
        }
        if(passwordVal != confirmationVal){
            password.error = "Passwords don't match"
            confirmation.error = "Passwords don't match"
            return false
        }
        return true
    }

    private fun checkOthers(name: EditText, address: EditText, phone: EditText): Boolean {
        val nameVal = name.text.toString().trim()
        val addressVal = address.text.toString().trim()
        val phoneVal = phone.text.toString().trim()
        if(nameVal.isEmpty()){
            name.error = "Field is empty"
            return false
        }
        if(addressVal.isEmpty()){
            address.error = "Field is empty"
            return false
        }
        if(phoneVal.isEmpty()){
            phone.error = "Field is empty"
            return false
        }
        return true
    }

    /**
     * Start the Main Activity.
     */
    private fun redirectToMainActivity(email: String){
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.putExtra("USER_EMAIL", email)
        startActivity(intent)
        activity?.finish()
    }

}