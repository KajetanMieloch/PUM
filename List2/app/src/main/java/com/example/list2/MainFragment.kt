package com.example.list2

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class MainFragment : Fragment(R.layout.fragment_main) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginButton = view.findViewById<Button>(R.id.button_to_login)
        val registerButton = view.findViewById<Button>(R.id.button_to_register)

        loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
        }

        registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_registerFragment)
        }
    }
}
