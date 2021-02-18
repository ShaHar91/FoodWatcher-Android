package com.shahar91.foodwatcher

import android.app.Application
import android.content.Context
import be.appwise.core.core.CoreApp

class MyApp : Application() {
    companion object {
        lateinit var mContext: Context

        fun getContext(): Context {
            return mContext
        }
    }

    override fun onCreate() {
        super.onCreate()

        mContext = this

        CoreApp.init(this)
            .initializeErrorActivity(BuildConfig.DEBUG)
            .initializeLogger()
            .build()
    }
}