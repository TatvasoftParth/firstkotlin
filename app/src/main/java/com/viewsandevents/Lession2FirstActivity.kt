package com.viewsandevents

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class Lession2FirstActivity : AppCompatActivity() {

    private val lifecycleLogs = mutableListOf<String>()
    private lateinit var listView: ListView
    private lateinit var btnViewGrid: Button
    private lateinit var btnShareLog: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lession2_first)

        listView = findViewById(R.id.lifecycle_list)
        btnViewGrid = findViewById(R.id.btn_view_grid)
        btnShareLog = findViewById(R.id.btn_share_log)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, lifecycleLogs)
        listView.adapter = adapter

        logLifecycleEvent(getString(R.string.app_created))

        btnViewGrid.setOnClickListener {
            val intent = Intent(this, Lession2SecondActivity::class.java)
            intent.putStringArrayListExtra("logs", ArrayList(lifecycleLogs))
            startActivity(intent)
        }

        btnShareLog.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, lifecycleLogs.joinToString("\n"))
            }
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_log_via)))
        }
    }

    override fun onStart() {
        super.onStart()
        logLifecycleEvent(getString(R.string.app_started))
    }

    override fun onRestart() {
        super.onRestart()
        logLifecycleEvent(getString(R.string.app_restarted))
    }

    override fun onResume() {
        super.onResume()
        logLifecycleEvent(getString(R.string.app_resumed))
    }

    override fun onPause() {
        super.onPause()
        logLifecycleEvent(getString(R.string.app_paused))
    }

    override fun onStop() {
        super.onStop()
        logLifecycleEvent(getString(R.string.app_stopped))
    }

    override fun onDestroy() {
        super.onDestroy()
        logLifecycleEvent(getString(R.string.app_destroyed))
    }

    private fun logLifecycleEvent(event: String) {
        val timestamp = System.currentTimeMillis()
        lifecycleLogs.add("$event at $timestamp")
        (listView.adapter as ArrayAdapter<*>).notifyDataSetChanged()
    }
}