package com.example.fin.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.fin.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

/**
 * `RegistrationFragment` is a Fragment used for user registration within the LoginActivity.
 */
class RegistrationFragment : Fragment() {
    private lateinit var userName: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var registrationButton: Button
    private lateinit var login: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_registration, container, false)

        // Initialize UI elements and Firebase authentication
        initValues(view)

        // Set click listeners for registration and login buttons
        registrationButton.setOnClickListener {
            updateUI(view)
        }

        login.setOnClickListener {
            // Navigate back to the login screen
            LoginActivity.isRegistrationScreen = false
            val intent = Intent(this.context, LoginActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    /**
     * Update the UI based on user input and attempt to create a new user using Firebase authentication.
     */
    private fun updateUI(view: View?) {
        val enteredUserName = userName.text.toString()
        val enteredPassword = password.text.toString()
        lateinit var userNameErrorMessage: TextView
        lateinit var passwordErrorMessage: TextView
        view?.let {
            userNameErrorMessage =
                view.findViewById(R.id.registration_user_name_error_message)
            passwordErrorMessage =
                view.findViewById(R.id.registration_password_error_message)
        }

        when {
            enteredUserName.isEmpty() -> {
                userNameErrorMessage.visibility = View.VISIBLE
                if (enteredPassword.isNotEmpty() && passwordErrorMessage.isVisible) {
                    passwordErrorMessage.visibility = View.GONE
                }
            }

            enteredPassword.isEmpty() -> {
                passwordErrorMessage.visibility = View.VISIBLE
                if (userNameErrorMessage.isVisible) {
                    userNameErrorMessage.visibility = View.GONE
                }
            }

            (enteredUserName.length < 10) -> {
                userNameErrorMessage.text = getString(R.string.valid_user_name)
                userNameErrorMessage.visibility = View.VISIBLE
                if (isNotValidPassword(enteredPassword) && passwordErrorMessage.isVisible) {
                    passwordErrorMessage.text = getString(R.string.valid_password)
                } else if (passwordErrorMessage.isVisible) {
                    passwordErrorMessage.visibility = View.GONE
                }
            }

            isNotValidPassword(enteredPassword) -> {
                passwordErrorMessage.text = getString(R.string.valid_password)
                passwordErrorMessage.visibility = View.VISIBLE
                if (userNameErrorMessage.isVisible) {
                    userNameErrorMessage.visibility = View.GONE
                }
            }

            else -> {
                // Validation passed, try to create a new user using Firebase authentication.
                passwordErrorMessage.visibility = View.GONE
                userNameErrorMessage.visibility = View.GONE
                auth.createUserWithEmailAndPassword("$enteredUserName@gmail.com", enteredPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Registration successful, navigate back to the login screen.
                            Log.d("mybug", "updateUI: inside task completed")
                            LoginActivity.isRegistrationScreen = false
                            val intent = Intent(this.context, LoginActivity::class.java)
                            startActivity(intent)
                        } else {
                            // Registration failed, show an error message.
                            Toast.makeText(
                                this.context,
                                "Failed to Register the User",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }
    }

    /**
     * Check if the provided password is not valid based on a regular expression pattern.
     */
    private fun isNotValidPassword(input: String): Boolean {
        val pattern = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[^A-Za-z0-9]).{7}$".toRegex()
        return !pattern.matches(input)
    }

    /**
     * Initialize UI elements and Firebase authentication.
     */
    private fun initValues(view: View?) {
        view?.let {
            userName = view.findViewById(R.id.registration_user_name)
            password = view.findViewById(R.id.registration_password)
            registrationButton = view.findViewById(R.id.registration_button)
            login = view.findViewById(R.id.login_clickable_text)
            auth = FirebaseAuth.getInstance()
        }
    }
}
