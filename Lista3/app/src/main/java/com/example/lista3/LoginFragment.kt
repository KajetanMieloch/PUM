package com.example.list3

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class LoginFragment : Fragment(R.layout.fragment_login) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginButton = view.findViewById<Button>(R.id.login_button)
        val registerButton = view.findViewById<Button>(R.id.register_button)

        val usernameInput = view.findViewById<EditText>(R.id.login_username_input)
        val passwordInput = view.findViewById<EditText>(R.id.login_password_input)

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()

            val user = Globals.users.find { it.username == username && it.password == password }
            if (user != null) {
                Globals.loggedInUsername = username
                findNavController().navigate(R.id.action_loginFragment_to_welcomeFragment)
            } else {
                Toast.makeText(requireContext(), "Nieprawidłowe dane logowania", Toast.LENGTH_SHORT).show()
            }
        }

        registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }
}
