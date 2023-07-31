package com.example.helper.ui.signInFeature

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.text.method.SingleLineTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.helper.R
import com.example.helper.databinding.FragmentLoginBinding
import com.example.helper.domen.models.AuthResult
import com.example.helper.ui.MainActivity
import com.example.helper.utils.BaseFragment
import com.example.helper.utils.hide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val authStatus = arguments?.getString("auth")

        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.authResult.collectLatest { auth ->
                        when (auth) {
                            is AuthResult.Unauthorized -> Log.d("LoginFragment", "Unauthorized")
                            is AuthResult.UnknownError -> Log.d("LoginFragment", "Error")
                            is AuthResult.Authorized -> findNavController().navigate(R.id.homeFragment)
                        }
                    }
                }
            }
        }

        (requireActivity() as MainActivity).binding.bottomNavigationView.hide()


        binding.signIn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.passwordEt.text.toString()
            var emailValidation: String? = ""

            viewModel.list.observe(viewLifecycleOwner) {
                for (i in it.info!!) {
                    if (i.email!!.contains(email)) {
                        emailValidation = i.name
                    }
                }
            }

            if (email == "admin@gmail.com" && password == "admin") {
                return@setOnClickListener
            }
            viewModel.signIn(email = email, password = password)

//            if (emailValidation?.isNotEmpty() == true && password.isNotEmpty()) {
//                viewModel.signIn(email = email, password = password)
//            }else{
//               Toast.makeText(requireActivity(),"Check your input data",Toast.LENGTH_LONG).show()
//            }
        }
        binding.signUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
