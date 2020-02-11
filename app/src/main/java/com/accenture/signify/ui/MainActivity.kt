package com.accenture.signify.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.accenture.signify.R
import kotlinx.android.synthetic.main.activity_lightfinder.*


class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lightfinder)
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        bottom_navigation_view.setupWithNavController(findNavController(R.id.navHostFragment))
    }
}