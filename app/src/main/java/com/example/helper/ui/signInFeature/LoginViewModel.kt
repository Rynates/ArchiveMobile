package com.example.helper.ui.signInFeature

import androidx.lifecycle.*
import com.example.helper.domen.models.AuthResult
import com.example.helper.domen.models.User
import com.example.helper.domen.repository.LoginRepository
import com.example.helper.domen.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val userRepository: UserRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _authResult = MutableStateFlow<AuthResult<Unit>>(AuthResult.Unauthorized())
    val authResult get() = _authResult.asSharedFlow()

    init {
        authenticate()
    }

    fun signIn(email:String,password:String){
        viewModelScope.launch {
            val auth = loginRepository.signIn(
                email = email,
                password = password
            )
            _authResult.value = auth
        }
    }
    fun logOut() {
        viewModelScope.launch {
            _authResult.value = AuthResult.Unauthorized()
        }
    }

    private fun authenticate(){
        viewModelScope.launch {
            val auth = loginRepository.authenticate()
            _authResult.value = auth
        }
    }

    val list = userRepository.getAllUsers().asLiveData()

}