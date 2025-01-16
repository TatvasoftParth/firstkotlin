package com.viewsandevents

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.viewsandevents.databinding.ActivityBookDetailBinding


class BookDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookDetailBinding
    private var bookId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBookDetailBinding
            .inflate(layoutInflater)
        setContentView(binding.root)

        retrieveDataFromIntent()
        binding.editButton.setOnClickListener {
            Log.d("ID@", "onCreate: $bookId")
            val lession = Intent(this, Lession3Activity::class.java)
            lession.putExtra("id", bookId.toString())
            lession.putExtra("book_name", binding.bookName.text)
            lession.putExtra("author_name", binding.authorName.text)
            lession.putExtra("genre", binding.genreValue.text)
            lession.putExtra("type", binding.bookType.text)
            lession.putExtra("launch_date", binding.launchDate.text)
            lession.putExtra("age_groups", binding.ageGroups.text)
            startActivity(lession)
        }
    }

    private fun retrieveDataFromIntent() {
        // Retrieve data from the intent
        Log.d("ID4", "retrieveDataFromIntent: ${intent.getStringExtra("id")}")
        bookId = intent.getStringExtra("id")?.toInt() ?: 0
        val bookName = intent.getStringExtra("book_name") ?: "N/A"
        val authorName = intent.getStringExtra("author_name") ?: "N/A"
        val genre = intent.getStringExtra("genre") ?: "N/A"
        val type = intent.getStringExtra("type") ?: "N/A"
        val launchDate = intent.getStringExtra("launch_date") ?: "N/A"
        val ageGroups = intent.getStringExtra("age_groups") ?: "N/A"

        // Set data to views
        binding.bookName.text = bookName
        binding.authorName.text = authorName
        binding.genreValue.text = genre
        binding.bookType.text = type
        binding.launchDate.text = launchDate
        binding.ageGroups.text = ageGroups
    }
}