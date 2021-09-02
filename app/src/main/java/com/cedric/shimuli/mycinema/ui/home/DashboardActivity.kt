package com.cedric.shimuli.mycinema.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.cedric.shimuli.mycinema.R
import com.cedric.shimuli.mycinema.databinding.ActivityDashboardBinding
import io.paperdb.Paper

class DashboardActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityDashboardBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Paper.init(this)

        // fragment navigation
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        val appBarConfiguration:AppBarConfiguration = AppBarConfiguration(setOf(
            R.id.homeFragment,
            R.id.favouriteFragment,
            R.id.bookingFragment,
            R.id.profileFragment
        ))

       binding.bottomNavigationView.setupWithNavController(navController)
        setupActionBarWithNavController(navController,appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}