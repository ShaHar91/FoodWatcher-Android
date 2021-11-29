package com.shahar91.foodwatcher

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import be.appwise.core.core.CoreApp
import com.akexorcist.localizationactivity.core.LanguageSetting
import com.akexorcist.localizationactivity.core.LocalizationApplicationDelegate
import com.shahar91.foodwatcher.di.KoinInitializer
import java.util.*

class MyApp : Application() {

    private val localizationDelegate = LocalizationApplicationDelegate()
    private lateinit var deviceLanguage: Locale

    companion object {
        lateinit var instance: MyApp
            private set

        /**
         * Supported languages for Orange: English, French, German & Dutch
         * English is default
         *
         * @param localeLanguage Locale
         * @return one of the supported languages
         */
        fun getDefaultLocaleForProject(localeLanguage: Locale): Locale {
            return if (localeLanguage.language == Locale("nl", "").language
//                || localeLanguage.language == Locale("fr", "").language ||
//                localeLanguage.language == Locale("de", "").language
            ) {
                localeLanguage
            } else {
                Locale.ENGLISH
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        initKoin()
        initCore()

        localizationDelegate.setDefaultLanguage(this, getDefaultLocaleForProject(deviceLanguage))
        // after the default language is set, the LanguageSetting needs to be adjusted in order to take effect through the complete app...
        // something strange, but this makes it work
        LanguageSetting.setLanguage(this, getDefaultLocaleForProject(deviceLanguage))
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

    override fun attachBaseContext(base: Context) {
        deviceLanguage = Locale.getDefault()
        localizationDelegate.setDefaultLanguage(base, deviceLanguage)
        super.attachBaseContext(localizationDelegate.attachBaseContext(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localizationDelegate.onConfigurationChanged(this)
    }

    override fun getApplicationContext(): Context {
        return localizationDelegate.getApplicationContext(super.getApplicationContext())
    }

    override fun getResources(): Resources {
        return localizationDelegate.getResources(baseContext)
    }
}