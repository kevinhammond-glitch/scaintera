package com.example.scainterclients.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class UserPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveUsername(username: String) {
        prefs.edit().putString("username", username).apply()
    }
    fun getUsername(): String? {
        return prefs.getString("username", null)
    }
    fun clearUsername() {
        prefs.edit().remove("username").apply()
    }
    fun savePassword(password: String) {
        prefs.edit().putString("password", password).apply()
    }
    fun getPassword(): String? {
        return prefs.getString("password", null)
    }
    fun clearPassword() {
        prefs.edit().remove("password").apply()
    }
    // Inside your UserPreferences class
    fun saveAssureId(id: Int) {
        prefs.edit {
            putInt("assureID", id)
        }
    }
}
