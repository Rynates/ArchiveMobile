package com.example.helper.ui.mapFeature.helpers

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.lifecycle.*
import com.example.helper.R

import com.example.helper.data.repository.DataStoreRepository
import com.example.helper.data.repository.GeofenceRepository
import com.example.helper.domen.models.ApiResponse
import com.example.helper.domen.models.GeoFence
import com.example.helper.domen.models.User
import com.example.helper.domen.repository.MapsRepository
import com.example.helper.domen.repository.UserRepository
import com.example.helper.utils.Constants
import com.example.helper.utils.GeofenceBroadcastReceiver
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.SphericalUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.sqrt

@HiltViewModel
class GeofenceViewModel @Inject constructor(
    application: Application,
    private val mapsRepository: MapsRepository,
    private val userRepository: UserRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val geofenceRepository: GeofenceRepository
) : AndroidViewModel(application) {

    val app = application


    // private var geofencingClient = LocationServices.getGeofencingClient(app.applicationContext)

    var id: Int = 0
    var geoName: String = ""
    var geoLocationName: String = ""
    var geoCountryCode: String = ""
    var geoLatLng: LatLng = LatLng(0.0, 0.0)
    var geoRadius: Double = 500.0
    var geoCitySelected = false
    var geofenceReady = false
    var geofencePrepared = false
    var createdBy: String = ""
    var userName: String = ""
    var time:String = ""
    var year:String = ""

    val user: MutableLiveData<User> = MutableLiveData()

    fun resetSharedValues() {
        id = 0
        geoName = "Default"
        geoCountryCode = ""
        geoLatLng = LatLng(0.0, 0.0)
        geoRadius = 500.0
        geoCitySelected = false
        geofenceReady = false
        geofencePrepared = false
        createdBy = ""
        time = ""
        year = ""
    }

    val readFirstLaunch = dataStoreRepository.readFirstLaunch
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    fun saveFirstLaunch(firstLaunch: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveFirstLaunch(firstLaunch)
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            val user = userRepository.getUserInfo()
            createdBy = user.info?.id ?:"User"
            userName = user.info?.name ?: "User"
        }
    }

    // Database locale
    val readGeofences = geofenceRepository.readGeofences.asLiveData()

    //remote db
    val listOfAllGeoFences = mapsRepository.getAllGeofences().asLiveData()

    //Database locale
    fun removeGeofence(geofenceEntity: GeoFence) =
        viewModelScope.launch(Dispatchers.IO) {
            geofenceRepository.removeGeofence(geofenceEntity)
        }

    fun removeFromRemote(geofenceEntity: GeoFence) =
        viewModelScope.launch(Dispatchers.IO){
            mapsRepository.deleteGeofence(geofenceEntity.geoId)
        }

    //add to remote db
    fun addGeoFence(geoFence: GeoFence) {
        viewModelScope.launch {
            mapsRepository.addGeoOnTheMap(geoFence)
        }
    }

    fun getUserOfGeofence(userId: String) = flow {
        val user = mapsRepository.getUserById(userId)
        emit(ApiResponse(success = true, info = user.info))
    }

    fun getAllLocations(context: Context) {
        viewModelScope.launch {
            mapsRepository.getAllGeofences().collect { list ->
                        list.info
                            ?.map { convertToLocation(it) }
//                            ?.map {
//                                if (it.checkIsInBound(it.radius, convertToLocation(i))) {
//                                    displayNotification(context = context, "Notify")
//                                } else {
//                                    return@map
//                                }
//                            }
//                    }
//                }
            }
        }
    }

    fun getLocationOfAllGeofences(context: Context) {
        viewModelScope.launch {
            geofenceRepository.readGeofences.collect { list ->
                list.map { convertToLocation(it) }.map {
                    if (it.checkIsInBound(
                            geoRadius,
                            it
                        )
                    ) displayNotification(context = context, "Notify")
                    else {
                        return@map
                    }
                }
            }
        }

    }

    private fun Location.checkIsInBound(radius: Double, center: Location): Boolean {
        return this.distanceTo(center) < radius
    }

    fun addGeofence(geofenceEntity: GeoFence) =
        viewModelScope.launch(Dispatchers.IO) {
            geofenceRepository.addGeofence(geofenceEntity)
        }

    fun convertToLocation(geofenceEntity: GeoFence): Location {
        val targetLocation = Location("")
        targetLocation.latitude = geofenceEntity.latitude
        targetLocation.longitude = geofenceEntity.longitude
        //val distanceInMeters: Float = targetLocation.distanceTo(myLocation)
        return targetLocation
    }

    fun addGeofenceToDatabase(location: LatLng, context: Context) {
        val geofenceEntity =
            GeoFence(
                id,
                geoName,
                geoLocationName,
                time,
                year,
                location.latitude,
                location.longitude,
                geoRadius,
                createdBy
            )

        addGeofence(geofenceEntity)
        addGeoFence(geofenceEntity)
        //getLocationOfAllGeofences(context)
        getAllLocations(context)
    }

    private fun displayNotification(context: Context, geofenceTransition: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(notificationManager)

        val notification =
            NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Geofence")
                .setContentText(geofenceTransition)
                .setContentIntent(setPendingIntent(id))
        notificationManager.notify(Constants.NOTIFICATION_ID, notification.build())
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            Constants.NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun setPendingIntent(geoId: Int): PendingIntent {
        val intent = Intent(app, GeofenceBroadcastReceiver::class.java)
        return PendingIntent.getBroadcast(
            app,
            geoId,
            intent,
            PendingIntent.FLAG_MUTABLE
        )
    }


    fun getBounds(center: LatLng, radius: Double): LatLngBounds {
        val distanceFromCenterToCorner = radius * sqrt(2.0)
        val southWestCorner =
            SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 225.0)
        val northEastCorner =
            SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 45.0)
        return LatLngBounds(southWestCorner, northEastCorner)
    }

    fun checkDeviceLocationSettings(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.isLocationEnabled
        } else {
            val mode: Int = Settings.Secure.getInt(
                context.contentResolver,
                Settings.Secure.ALLOWED_GEOLOCATION_ORIGINS,
                Settings.Secure.LOCATION_MODE_OFF
            )
            mode != Settings.Secure.LOCATION_MODE_OFF
        }
    }

}