package com.viewsandevents

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.demoapp.ImageAdapter
import com.viewsandevents.databinding.ActivityLesson6FirstBinding
import java.io.File

class Lesson6FirstActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLesson6FirstBinding
    private var folderPath: String = Environment.getExternalStorageDirectory().absolutePath + "/Pictures"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLesson6FirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Open settings
        binding.buttonOpenSettings.setOnClickListener {
            val intent = Intent(this, Lesson6SecondActivity::class.java)
            startActivity(intent)
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val images = loadImagesFromFolder(folderPath)
        binding.recyclerViewImages.layoutManager = GridLayoutManager(this, 3) // 3 columns
        binding.recyclerViewImages.adapter = ImageAdapter(this, images)
    }

    private fun loadImagesFromFolder(folder: String): List<File?> {
        val file = File(folder)
        return file.listFiles { f -> f.extension in listOf("jpg", "png") }?.toList() ?: emptyList()
    }
}