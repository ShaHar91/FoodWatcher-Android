package com.shahar91.foodwatcher.ui

import android.os.Bundle
import androidx.navigation.NavHost
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import be.appwise.core.ui.base.BaseActivity
import com.shahar91.foodwatcher.R

class MainActivity : BaseActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val host = supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment) as NavHost ? ?: return
        val navController = host.navController

//        appBarConfiguration = AppBarConfiguration(setOf(R.id.foodItemListFragment))

        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onNavigateUp(): Boolean {
        return findNavController(R.id.my_nav_host_fragment).navigateUp()
    }
}