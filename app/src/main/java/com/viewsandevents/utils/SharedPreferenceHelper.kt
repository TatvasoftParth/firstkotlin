package com.viewsandevents.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(private val context: Context, private val prefName: String) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(prefName, Context.MODE_PRIVATE)

    // Save a string value
    fun putString(key: String, value: String?) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    // Get a string value
    fun getString(key: String, defaultValue: String? = null): String? {
        return sharedPreferences.getString(key, defaultValue)
    }
}