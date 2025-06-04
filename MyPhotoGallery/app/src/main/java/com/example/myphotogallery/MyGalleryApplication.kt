package com.example.myphotogallery

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.myphotogallery.ui.SettingsActivity

class MyGalleryApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        applyAppTheme()
    }

    private fun applyAppTheme() {
        val sharedPreferences = getSharedPreferences(SettingsActivity.PREFS_NAME, Context.MODE_PRIVATE)
        when (sharedPreferences.getInt(SettingsActivity.KEY_THEME, SettingsActivity.THEME_SYSTEM)) { // Default to system
            SettingsActivity.THEME_LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            SettingsActivity.THEME_DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            SettingsActivity.THEME_SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}