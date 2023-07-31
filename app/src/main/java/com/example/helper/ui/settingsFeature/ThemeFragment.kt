package com.example.helper.ui.settingsFeature

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.helper.R
import com.example.helper.utils.ThemeHelper

class ThemeFragment:PreferenceFragmentCompat() {

    companion object{
        const val TAG = "ThemeFragment"
    }
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences,rootKey)

        val themePref = findPreference<ListPreference>("themePref")
        if(themePref != null){
            themePref.onPreferenceChangeListener = Preference.OnPreferenceChangeListener{ pref,value ->
                val themeOpt = value as String
                ThemeHelper.applyThemeToApp(themeOpt)
                true
            }
        }
    }
}