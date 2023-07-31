package com.example.helper.ui.signUpFeature

import androidx.lifecycle.*
import com.example.helper.domen.repository.SignUpRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpRepository: SignUpRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {


      fun signUp(username:String,password:String,email:String) {
        viewModelScope.launch {
            signUpRepository.signUp(
                email = email,
                password = password,
                username = username
            )
        }
    }

}




