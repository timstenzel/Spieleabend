package de.stenzel.tim.spieleabend.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import de.stenzel.tim.spieleabend.R

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val nightModeEnabled = sharedPreferences.getBoolean(getString(R.string.theme_key), false)
        if (nightModeEnabled) {
            //switch theme to dark
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            //switch theme to light
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        }
        val navController = Navigation.findNavController(this, R.id.act_main_nav_host)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation_bar)
        NavigationUI.setupWithNavController(bottomNav, navController)
    }
}