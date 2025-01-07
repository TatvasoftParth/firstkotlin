package com.viewsandevents

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.viewsandevents.databinding.ActivityLession2FirstBinding

class Lession2FirstActivity : AppCompatActivity() {

    private val lifecycleLogs = mutableListOf<String>()
    private lateinit var binding: ActivityLession2FirstBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLession2FirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, lifecycleLogs)
        binding.lifecycleList.adapter = adapter

        logLifecycleEvent(getString(R.string.app_created))

        binding.btnViewGrid.setOnClickListener {
            val intent = Intent(this, Lession2SecondActivity::class.java)
            intent.putStringArrayListExtra("logs", ArrayList(lifecycleLogs))
            startActivity(intent)
        }

        binding.btnShareLog.setOnClickListener {
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
        (binding.lifecycleList.adapter as ArrayAdapter<*>).notifyDataSetChanged()
    }
}