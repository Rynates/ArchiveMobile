package com.example.helper.ui.profileFeature

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helper.domen.models.GeoFence
import com.example.helper.domen.models.User
import com.example.helper.domen.repository.MapsRepository
import com.example.helper.domen.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val mapsRepository: MapsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _user = MutableSharedFlow<User>()
    val user = _user.asSharedFlow()

    private var _geofences = MutableSharedFlow<List<GeoFence>>()
    val geofences = _geofences.asSharedFlow()

    fun getCurrentUser(){
        viewModelScope.launch {
            val user = userRepository.getUserInfo()
            user.info?.let { _user.emit(it) }
        }
    }

    fun updateCurrentUser(user:User){
        viewModelScope.launch {
           val updateUser = userRepository.updateUserInfo(user = user)
            updateUser.info?.let { _user.emit(it) }
        }
    }

    fun getGeoFencesByUserId(userId:String){
        viewModelScope.launch {
            val listOfGeofence = mapsRepository.getGeofenceByUserId(userId = userId)
            listOfGeofence.info?.let { _geofences.emit(it) }
        }
    }

    fun getOtherUserByName(userName:String){
        viewModelScope.launch {
            val user = userRepository.getOtherUserByName(userName = userName)
            user.info?.let { _user.emit(it) }
        }
    }
    fun getUserById(userId:String){
        viewModelScope.launch {
            val user = userRepository.getUserById(userId)
            user.info?.let { _user.emit(it) }
        }
    }
}