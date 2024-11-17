package com.example.list2

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class WelcomeFragment : Fragment(R.layout.fragment_welcome) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString("username") ?: "Użytkowniku"

        val welcomeMessage = view.findViewById<TextView>(R.id.welcome_message)
        welcomeMessage.text = "Witaj, $username!"

        val logoutButton = view.findViewById<Button>(R.id.logout_button)
        logoutButton.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_mainFragment)
        }
    }
}
