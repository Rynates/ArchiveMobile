package com.example.helper.ui.signUpFeature

import android.os.Bundle
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
import com.example.helper.databinding.FragmentSignUpBinding
import com.example.helper.domen.models.AuthResult
import com.example.helper.ui.MainActivity
import com.example.helper.utils.BaseFragment
import com.example.helper.utils.hide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : BaseFragment() {

    private var _signUpFragmentBinding: FragmentSignUpBinding? = null
    private val signUpFragmentBinding get() = _signUpFragmentBinding!!

    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _signUpFragmentBinding = FragmentSignUpBinding.inflate(inflater, container, false)
        return signUpFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        signUpFragmentBinding.registration.setOnClickListener {
//            findNavController().popBackStack()

        (requireActivity() as MainActivity).binding.bottomNavigationView.hide()

        signUpFragmentBinding.registration.setOnClickListener {

            val email = signUpFragmentBinding.emailEt.text.toString()
            val userName = signUpFragmentBinding.usernameEt.text.toString()
            val password = signUpFragmentBinding.passwordEt.text.toString()
            if(userName.isEmpty() || password.isEmpty() || email.isEmpty() || !email.contains("@")) {
                Toast.makeText(requireActivity(),"Be sure all fields are filled in a correct form:Password and Username should have at least 7 characters,email should be written in a standard email form (**@gmail.com)",Toast.LENGTH_LONG).show()
            }
            if(userName.length < 7 && password.length < 7 ){
                Toast.makeText(requireActivity(),"UserName and Password should have at least 7 characters",Toast.LENGTH_LONG).show()
            }
            else {
                viewModel.signUp(username = userName, password = password, email = email)
                findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _signUpFragmentBinding = null
    }
}