package com.example.helper.ui.settingsFeature

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.ConfigurationCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.helper.ui.MainActivity
import com.example.helper.R
import com.example.helper.databinding.FragmentSettingsBinding
import com.example.helper.databinding.InfoDialogLayoutBinding
import com.example.helper.databinding.MaterialDialogLayoutBinding
import com.example.helper.ui.signInFeature.LoginViewModel
import com.example.helper.utils.BaseFragment
import com.example.helper.utils.LocaleUtil
import com.example.helper.utils.show
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class SettingsFragment : BaseFragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder

    private val loginViewModel:LoginViewModel by viewModels()
   // private lateinit var customAlertDialogView: View
    private lateinit var firstLocaleCode: String
    private lateinit var secondLocaleCode: String
    private lateinit var currentSystemLocaleCode: String

    private lateinit var selectedLang: String
   // private var selectedLangIndex: Int = 0
    private val languagesDialog = arrayOf("English","Russian")
    lateinit var  preferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = com.example.helper.databinding.FragmentSettingsBinding.inflate(inflater, container, false)
        (requireActivity() as MainActivity).binding.bottomNavigationView.show()

        preferences = requireActivity().getSharedPreferences("SHARED_LANG", Context.MODE_PRIVATE)

        currentSystemLocaleCode = ConfigurationCompat.getLocales(Resources.getSystem().configuration).get(0)!!.language
        if((activity as MainActivity).storage.getPreferredLocale() == LocaleUtil.OPTION_PHONE_LANGUAGE){
            if(currentSystemLocaleCode in LocaleUtil.supportedLocales){
               binding.languageSet.text = getString(R.string.language, Locale(currentSystemLocaleCode).displayLanguage)
            } else {
                binding.languageSet.text = "English"
            }
        } else {
            if(currentSystemLocaleCode == (activity as MainActivity).storage.getPreferredLocale()){
                binding.languageSet.text = getString(R.string.language, Locale(currentSystemLocaleCode).displayLanguage)
            } else {
                binding.languageSet.text = Locale((activity as MainActivity).storage.getPreferredLocale()).displayLanguage
            }
        }
        firstLocaleCode = if (currentSystemLocaleCode in LocaleUtil.supportedLocales) {
            currentSystemLocaleCode
        } else {
            if ((activity as MainActivity).storage.getPreferredLocale() == LocaleUtil.OPTION_PHONE_LANGUAGE) {
                "en"
            } else {
                (activity as MainActivity).storage.getPreferredLocale()
            }
        }
        secondLocaleCode = getSecondLanguageCode()


        // secondLocaleCode = getSecondLanguageCode()
        materialAlertDialogBuilder =
            MaterialAlertDialogBuilder(this@SettingsFragment.requireContext())
        binding.tmAppthemeSettings.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_themeFragment)
        }
        binding.mkLanguageBtnSettings.setOnClickListener {
            // customAlertDialogView = LayoutInflater.from(requireActivity()).inflate(R.layout.languages_layout, container, false)
            launchLanguageAlertDialog()
        }

        binding.tmSignout.setOnClickListener {
            loginViewModel.logOut()
            findNavController().navigate(R.id.action_settingsFragment_to_loginFragment)
        }

        binding.mkHelpBtnSettings.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_infoFragment)
        }

        return binding.root
    }

    private fun getSecondLanguageCode(): String {
        return if(firstLocaleCode == "en") "ru" else "en"
    }

    private fun getLanguageNameByCode(code: String): String {
        val tempLocale = Locale(code)
        return tempLocale.getDisplayLanguage(tempLocale)
    }

    private fun updateAppLocale(locale: String) {

        (activity as MainActivity).storage.setPreferredLocale(locale)
        LocaleUtil.applyLocalizedContext(requireActivity().applicationContext, locale)
    }

    private fun launchLanguageAlertDialog() {
        val preferences: SharedPreferences =
            requireActivity().getSharedPreferences("SHARED_LANG", Context.MODE_PRIVATE)
        var selectedLangIndex = preferences.getInt("LANG", 0)
        selectedLang = languagesDialog[selectedLangIndex]
        materialAlertDialogBuilder
            .setTitle("Choose the language")
            .setSingleChoiceItems(languagesDialog, selectedLangIndex) { dialog_, which ->
                selectedLangIndex = which
                selectedLang = languagesDialog[which]
            }
            .setPositiveButton("Select") { dialog, _ ->
                if (selectedLangIndex == 0) {
                    updateAppLocale(LocaleUtil.OPTION_PHONE_LANGUAGE)
                    requireActivity().recreate()
                    with(preferences.edit()) {
                        putInt("LANG", selectedLangIndex)
                        apply()
                    }
                }
                if (selectedLangIndex == 1) {
                    updateAppLocale(secondLocaleCode)
                    requireActivity().recreate()
                    with(preferences.edit()) {
                        putInt("LANG", selectedLangIndex)
                        apply()
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

}