package com.rahmath888hussain.tapcounter

import android.content.Context
import android.content.SharedPreferences

class SharedPrefs private constructor(context: Context){
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "my_app_prefs"
        @Volatile private var instance: SharedPrefs? = null

        fun getInstance(context: Context): SharedPrefs {
            return instance ?: synchronized(this) {
                instance ?: SharedPrefs(context).also { instance = it }
            }
        }
    }

    fun saveString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String? = null): String? {
        return sharedPreferences.getString(key, defaultValue)
    }

    fun saveInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

}