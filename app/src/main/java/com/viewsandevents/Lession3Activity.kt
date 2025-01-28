package com.viewsandevents

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentTransaction
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
            val id = intent.getStringExtra("id") ?: "0"
            val bookName = intent.getStringExtra("book_name")
            val authorName = intent.getStringExtra("author_name")
            val genre = intent.getStringExtra("genre")
            val type = intent.getStringExtra("type")
            val launchDate = intent.getStringExtra("launch_date")
            val ageGroups = intent.getStringExtra("age_groups")
            val bundle = Bundle()

            Log.d("ID1", "onCreateId: ${intent.getStringExtra("id")}")
            bundle.putString("id", id)
            bundle.putString("book_name", bookName)
            bundle.putString("author_name", authorName)
            bundle.putString("genre", genre)
            bundle.putString("type", type)
            bundle.putString("launch_date", launchDate)
            bundle.putString("age_groups", ageGroups)
            val fragInfo = AddBookFragment()
            fragInfo.setArguments(bundle)
            val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, fragInfo)
            fragmentTransaction.commit()
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
            R.id.add_user_detail -> {
                val intent = Intent(this, Lession4FirstActivity::class.java)
                startActivity(intent)
            }
            R.id.list_bookmarks -> {
                val intent = Intent(this, Lesson5FirstActivity::class.java)
                startActivity(intent)
            }
            R.id.list_image -> {
                val intent = Intent(this, Lesson6FirstActivity::class.java)
                startActivity(intent)
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