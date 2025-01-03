package com.viewsandevents

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.GridView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class Lession2SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lession2_second)
        val gridView: GridView = findViewById(R.id.grid_view)
        val logs = intent.getStringArrayListExtra("logs") ?: ArrayList()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, logs)
        gridView.adapter = adapter
    }
}