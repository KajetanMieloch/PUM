package com.example.list2

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class RegisterFragment : Fragment(R.layout.fragment_register) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val usernameInput = view.findViewById<EditText>(R.id.register_username_input)
        val passwordInput = view.findViewById<EditText>(R.id.register_password_input)
        val confirmPasswordInput = view.findViewById<EditText>(R.id.register_confirm_password_input)
        val registerButton = view.findViewById<Button>(R.id.register_fragment_button)

        registerButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()
            val confirmPassword = confirmPasswordInput.text.toString()

            // Walidacja danych
            when {
                username.isBlank() || password.isBlank() || confirmPassword.isBlank() -> {
                    Toast.makeText(requireContext(), "Wszystkie pola są wymagane", Toast.LENGTH_SHORT).show()
                }
                password != confirmPassword -> {
                    Toast.makeText(requireContext(), "Hasła nie są zgodne", Toast.LENGTH_SHORT).show()
                }
                Globals.users.any { it.username == username } -> {
                    Toast.makeText(requireContext(), "Użytkownik o tej nazwie już istnieje", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Dodanie nowego użytkownika
                    Globals.users.add(User(username, password))
                    Toast.makeText(requireContext(), "Rejestracja zakończona sukcesem!", Toast.LENGTH_SHORT).show()

                    // Powrót na ekran logowania
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
            }
        }
    }
}
