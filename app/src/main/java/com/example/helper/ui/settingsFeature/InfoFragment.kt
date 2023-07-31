package com.example.helper.ui.settingsFeature

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.ConfigurationCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.helper.R
import com.example.helper.databinding.FragmentSettingsBinding
import com.example.helper.databinding.InfoDialogLayoutBinding
import com.example.helper.ui.MainActivity
import com.example.helper.ui.signInFeature.LoginViewModel
import com.example.helper.utils.BaseFragment
import com.example.helper.utils.LocaleUtil
import com.example.helper.utils.show
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class InfoFragment : BaseFragment() {

    private var _binding: InfoDialogLayoutBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = InfoDialogLayoutBinding.inflate(inflater, container, false)
        (requireActivity() as MainActivity).binding.bottomNavigationView.show()


        return binding.root
    }



}