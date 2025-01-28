package com.viewsandevents

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.viewsandevents.databinding.ActivityLesson6SecondBinding

class Lesson6SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLesson6SecondBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLesson6SecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val colors = resources.getStringArray(R.array.color_options)
        val folders = resources.getStringArray(R.array.folder_options)

        binding.spinnerColor.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, colors)
        binding.spinnerFolder.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, folders)

        binding.buttonSave.setOnClickListener {
            // Save preferences (optional: use SharedPreferences)
            finish()
        }
    }
}