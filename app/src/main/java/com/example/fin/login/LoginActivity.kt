package com.example.fin.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.fin.MainActivity
import com.example.fin.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

/**
 * `LoginActivity` is the main activity for user authentication and login.
 */
class LoginActivity : AppCompatActivity() {
    private lateinit var userName: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var loginButton: Button
    private lateinit var register: TextView
    private lateinit var auth: FirebaseAuth

    override fun onStart() {
        super.onStart()
        // Check if the user is already signed in and redirect to MainActivity if logged in.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val mainActivityIntent = Intent(this, MainActivity::class.java)
            startActivity(mainActivityIntent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize UI elements, Firebase authentication, and handle saved instance state.
        initValues()

        if (savedInstanceState != null) {
            val savedUserName = savedInstanceState.getString(USER_NAME, null)
            val savedPassword = savedInstanceState.getString(PASSWORD, null)
            val savedState = savedInstanceState.getBoolean(IS_REGISTRATION_SCREEN, false)
            when {
                savedState -> {
                    openRegistrationFragment()
                }

                savedUserName != null -> {
                    userName.setText(savedUserName)
                }

                savedPassword != null -> {
                    password.setText(savedPassword)
                }
            }
        }

        // Set click listeners for login and registration buttons.
        loginButton.setOnClickListener {
            setupLoginScreenUI()
        }

        register.setOnClickListener {
            openRegistrationFragment()
        }
    }

    /**
     * Open the registration fragment by hiding the login UI and showing the registration fragment.
     */
    private fun openRegistrationFragment() {
        isRegistrationScreen = true
        val loginActivity: LinearLayout = findViewById(R.id.login_container)
        loginActivity.visibility = View.GONE
        val registrationFragment: View = findViewById(R.id.registration_fragment_container)
        registrationFragment.visibility = View.VISIBLE
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(
            R.id.registration_fragment_container,
            RegistrationFragment()
        ).commit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(USER_NAME, userName.text.toString())
        outState.putString(PASSWORD, password.text.toString())
        outState.putBoolean(IS_REGISTRATION_SCREEN, isRegistrationScreen)
    }

    /**
     * Validate user input and attempt to log in the user using Firebase authentication.
     */
    private fun setupLoginScreenUI() {
        val enteredUserName = userName.text.toString()
        val enteredPassword = password.text.toString()
        val userNameErrorMessage: TextView = findViewById(R.id.user_name_error_message)
        val passwordErrorMessage: TextView = findViewById(R.id.password_error_message)

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
                // Validation passed, try to authenticate the user using Firebase.
                passwordErrorMessage.visibility = View.GONE
                userNameErrorMessage.visibility = View.GONE
                auth.signInWithEmailAndPassword("$enteredUserName@gmail.com", enteredPassword)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Login successful, navigate to MainActivity.
                            Log.d("mybug", "setupLoginScreenUI: login task")
                            val mainActivityIntent = Intent(this, MainActivity::class.java)
                            startActivity(mainActivityIntent)
                            finish()
                        } else {
                            // Login failed, show an error message.
                            Toast.makeText(this, "Failed to login", Toast.LENGTH_LONG).show()
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

    private fun initValues() {
        // Initialize Firebase authentication, UI elements, and the registration screen flag.
        auth = FirebaseAuth.getInstance()
        userName = findViewById(R.id.user_name)
        password = findViewById(R.id.password)
        loginButton = findViewById(R.id.login_button)
        register = findViewById(R.id.register_clickable_text)
    }

    companion object {
        private const val USER_NAME: String = "userName"
        private const val PASSWORD: String = "password"
        private const val IS_REGISTRATION_SCREEN = "is registration screen open"
        var isRegistrationScreen = false
    }
}
