package com.viewsandevents

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class Screen2Activity : AppCompatActivity() {
    private lateinit var searchEditText: TextInputEditText
    private lateinit var option1EditText: TextInputEditText
    private lateinit var option2EditText: TextInputEditText
    private lateinit var option3EditText: TextInputEditText
    private lateinit var option4EditText: TextInputEditText
    private lateinit var authorizeButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_screen2)
        initializeView()

        authorizeButton.setOnClickListener {
            onClickAuthorize()
        }

    }

    private fun onClickAuthorize() {
        val myIntent = Intent(
            this,
            Screen3Activity::class.java
        )
        startActivity(myIntent)
    }

    private fun initializeView() {
        searchEditText = findViewById(R.id.textField)
        option1EditText = findViewById(R.id.option1)
        option2EditText = findViewById(R.id.option2)
        option3EditText = findViewById(R.id.option3)
        option4EditText = findViewById(R.id.option4)
        authorizeButton = findViewById(R.id.btnAuth)// Set up the age picker
    }
}