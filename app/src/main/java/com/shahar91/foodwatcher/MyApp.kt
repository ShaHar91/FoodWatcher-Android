package com.shahar91.foodwatcher

import android.app.Application
import be.appwise.core.core.CoreApp

class MyApp : Application() {
    companion object {

        lateinit var instance: MyApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        CoreApp.init(this)
            .initializeErrorActivity(BuildConfig.DEBUG)
            .initializeLogger(getString(R.string.app_name), BuildConfig.DEBUG)
            .build()
    }
}