package com.viewsandevents

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Screen3Activity : AppCompatActivity() {
    private lateinit var l1: LinearLayout
    private lateinit var l2: LinearLayout
    private lateinit var r1: LinearLayout
    private lateinit var r2: LinearLayout
    private lateinit var btnTop: LinearLayout
    private lateinit var btnCenter: LinearLayout
    private lateinit var btnBottom: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_screen3)
        initializeView()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnTop.setOnClickListener {
            l2.visibility = if (l2.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            r2.visibility =  if (r2.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        btnCenter.setOnClickListener{
            l1.visibility = View.VISIBLE
            l2.visibility = View.VISIBLE
            r1.visibility = View.VISIBLE
            r2.visibility = View.VISIBLE
        }
        btnBottom.setOnClickListener{
            l1.visibility = if(l1.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            r1.visibility = if(r1.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
    }

    private fun initializeView() {
        btnTop = findViewById(R.id.btnTop)
        btnCenter = findViewById(R.id.view)
        btnBottom = findViewById(R.id.btnBottom)
        l1 = findViewById(R.id.linearLayout)
        l2 = findViewById(R.id.linearLayout3)
        r1 = findViewById(R.id.linearLayout2)
        r2 = findViewById(R.id.linearLayout4)
    }
}