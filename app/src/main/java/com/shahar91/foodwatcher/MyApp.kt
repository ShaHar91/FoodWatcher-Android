package com.shahar91.foodwatcher

import android.app.Application
import be.appwise.core.core.CoreApp
import com.shahar91.foodwatcher.di.KoinInitializer

class MyApp : Application() {
    companion object {

        lateinit var instance: MyApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        initKoin()
        initCore()
    }

    private fun initKoin() {
        KoinInitializer.init(this)
    }

    private fun initCore() {
        CoreApp.init(this)
            .initializeErrorActivity(BuildConfig.DEBUG)
            .initializeLogger(getString(R.string.app_name), BuildConfig.DEBUG)
            .build()
    }
}