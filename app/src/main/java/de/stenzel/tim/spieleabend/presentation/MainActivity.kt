package de.stenzel.tim.spieleabend.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import de.stenzel.tim.spieleabend.R

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = Navigation.findNavController(this, R.id.act_main_nav_host)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation_bar)
        NavigationUI.setupWithNavController(bottomNav, navController)
    }
}