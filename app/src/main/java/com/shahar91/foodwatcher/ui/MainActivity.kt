package com.shahar91.foodwatcher.ui

import android.os.Bundle
import be.appwise.core.ui.base.BaseActivity
import com.shahar91.foodwatcher.R

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}