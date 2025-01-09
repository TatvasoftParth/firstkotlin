package com.viewsandevents

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.viewsandevents.databinding.ActivityLession3Binding

class Lession3Activity : AppCompatActivity() {

    private lateinit var binding: ActivityLession3Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLession3Binding
            .inflate(layoutInflater)
        setContentView(binding.root)
        @Suppress("DEPRECATION")
        window.statusBarColor = ContextCompat.getColor(this, R.color.darkPrimary)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.topAppBar.setNavigationOnClickListener {
            binding.myDrawerLayout.openDrawer(GravityCompat.START)
        }

        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AddBookFragment()).commit()
            binding.navigationView.setCheckedItem(R.id.add_book)
        }

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            onSelectedMenuItam(menuItem)
        }
    }

    private fun onSelectedMenuItam(menuItem: MenuItem): Boolean {
        // Handle menu item selected
        when(menuItem.itemId) {
            R.id.add_book -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, AddBookFragment()).commit()
                binding.navigationView.setCheckedItem(R.id.add_book)
            }
            R.id.list_book -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, BookListFragment()).commit()
                binding.navigationView.setCheckedItem(R.id.list_book)
            }
        }
        menuItem.isChecked = true
        binding.myDrawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (binding.myDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.myDrawerLayout.closeDrawer(GravityCompat.START)
        } else {
            @Suppress("DEPRECATION")
            super.onBackPressed()
        }
    }
}