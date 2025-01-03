package com.viewsandevents

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private lateinit var userNameEditText: EditText
    private lateinit var userEmailEditText: EditText
    private lateinit var userPasswordEditText: EditText
    private lateinit var contactNumberEditText: EditText
    private lateinit var agePickerNumberPicker: NumberPicker
    private lateinit var nextButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        initializeView()

        nextButton.setOnClickListener {
            nextButtonAction()
        }
    }

    private fun nextButtonAction() {
        // Get user input
        val name = userNameEditText.text.toString().trim()
        val email = userEmailEditText.text.toString().trim()
        val password = userPasswordEditText.text.toString()
        val contact = contactNumberEditText.text.toString()
        val age = agePickerNumberPicker.value

        // Validate user input
        when {
            name.isEmpty() -> showToast(getString(R.string.user_name_validaiton_error))
            email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> showToast(
                getString(
                    R.string.email_validation_error
                ))
            password.isEmpty() || password.length < 6 -> showToast(getString(R.string.password_validation_error))
            contact.isEmpty() || contact.length < 10 -> showToast(getString(R.string.contact_number_validation_error))
            else -> {
                showToast(getString(R.string.age_validation_success_message) + "$age")
                val myIntent = Intent(
                    this,
                    Screen2Activity::class.java
                )
                startActivity(myIntent)
            }
        }
    }

    private fun initializeView() {
        userNameEditText = findViewById(R.id.etUserName)
        userEmailEditText = findViewById(R.id.etUserEmail)
        userPasswordEditText = findViewById(R.id.etUserPassword)
        contactNumberEditText = findViewById(R.id.etContactNumber)
        agePickerNumberPicker = findViewById(R.id.agePicker)
        nextButton = findViewById(R.id.btnNext)// Set up the age picker
        agePickerNumberPicker.minValue = 18
        agePickerNumberPicker.maxValue = 100
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}