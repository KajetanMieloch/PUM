package com.example.list2

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class LoginFragment : Fragment(R.layout.fragment_login) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginButton = view.findViewById<Button>(R.id.login_button)
        val registerButton = view.findViewById<Button>(R.id.register_button)

        // Obsługa przycisku "Zaloguj"
        loginButton.setOnClickListener {
            // Dodaj logikę logowania (np. sprawdzanie danych użytkownika)
            // Na razie możemy wyświetlić komunikat lub przejść do innego fragmentu
        }

        loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_welcomeFragment)
        }

        // Obsługa przycisku "Zarejestruj"
        registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }
}
