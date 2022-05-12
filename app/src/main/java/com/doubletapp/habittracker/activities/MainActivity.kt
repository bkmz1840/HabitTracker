package com.doubletapp.habittracker.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.doubletapp.habittracker.R
import com.doubletapp.habittracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.activityMainToolbar)
        supportActionBar?.title = resources.getString(R.string.app_name)

        val drawerLayout = binding.navigationDrawerLayout
        val navView = binding.navigationView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_add_habit, R.id.nav_app_info),
            drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        loadAvatar(navView.getHeaderView(0).findViewById(R.id.nav_header_avatar))
    }

    private fun loadAvatar(source: ImageView) {
        Glide.with(this)
            .load("https://www.fivechanges.com/wp-content/uploads/2014/09/astronaut-300x300.jpg")
            .override(200, 200)
            .placeholder(R.drawable.avatar_placeholder)
            .error(R.mipmap.ic_launcher_round)
            .circleCrop()
            .into(source)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}