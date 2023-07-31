package com.example.helper

import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import com.example.helper.utils.LocaleUtil
import com.example.helper.utils.Storage
import com.example.helper.utils.ThemeHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp: Application() {

    val storage: Storage by lazy {
        Storage(this)
    }
    override fun onCreate() {
        super.onCreate()
        initThemeSharedPrefs()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleUtil.getLocalizedContext(base,
            com.example.helper.utils.Storage(base).getPreferredLocale()))
    }

    fun initThemeSharedPrefs(){
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this)
        val themePref =
            sharedPreferences.getString("themePref", ThemeHelper.DEFAULT_MODE)
        ThemeHelper.applyThemeToApp(themePref!!)
    }

}