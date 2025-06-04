package com.example.myphotogallery.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.myphotogallery.R
import com.example.myphotogallery.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val PREFS_NAME = "theme_prefs"
        const val KEY_THEME = "prefs_theme"
        const val THEME_LIGHT = 0
        const val THEME_DARK = 1
        const val THEME_SYSTEM = 2 // Optional: For system default
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        setupToolbar()
        setupThemeSwitch()
        setupAboutSection()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarSettings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarSettings.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupThemeSwitch() {
        // Initialize switch based on current applied mode
        when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_NO -> binding.switchDarkMode.isChecked = false
            AppCompatDelegate.MODE_NIGHT_YES -> binding.switchDarkMode.isChecked = true
            // Handle MODE_NIGHT_FOLLOW_SYSTEM or other states if you support them
            else -> { // Default to system or light if not explicitly set. For this switch, let's map system to "off"
                val currentNightMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
                binding.switchDarkMode.isChecked = currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES
            }
        }


        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPreferences.edit().putInt(KEY_THEME, THEME_DARK).apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPreferences.edit().putInt(KEY_THEME, THEME_LIGHT).apply()
            }
            // No need to recreate SettingsActivity itself typically,
            // as Material components often adapt. Other activities will need to be recreated or adapt.
        }
    }

    private fun setupAboutSection() {
        binding.textViewAbout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(R.string.about_dialog_title)
                .setMessage(R.string.about_dialog_message)
                .setPositiveButton(R.string.ok, null)
                .show()
        }
    }
}