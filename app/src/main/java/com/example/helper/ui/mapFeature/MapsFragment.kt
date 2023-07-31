package com.example.helper.ui.mapFeature

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.helper.R
import com.example.helper.databinding.FragmentMapsBinding
import com.example.helper.databinding.MaterialDialogLayoutBinding
import com.example.helper.ui.MainActivity
import com.example.helper.ui.homeFeature.HomeFragment
import com.example.helper.ui.mapFeature.helpers.GeofenceViewModel
import com.example.helper.utils.*
import com.example.helper.utils.Permissions.hasBackgroundLocationPermission
import com.example.helper.utils.Permissions.requestsBackgroundLocationPermission
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt


@Suppress("DEPRECATION")
@AndroidEntryPoint
class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener,
    EasyPermissions.PermissionCallbacks {

    private lateinit var map: GoogleMap
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var circle: Circle
    private val args by navArgs<MapsFragmentArgs>()
    private lateinit var adapter: MarkerInfoWindowAdapter
    private lateinit var customAlertDialogView: MaterialDialogLayoutBinding


    private val geofenceViewModel: GeofenceViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBackPressedHandler<HomeFragment>(this, requireActivity(), viewLifecycleOwner)
        (requireActivity() as MainActivity).binding.bottomNavigationView.show()

        binding.addGeofenceFab.setOnClickListener {
            findNavController().navigate(R.id.action_mapsFragment_to_placesFragment)
        }

        binding.geofencesFab.setOnClickListener {
            findNavController().navigate(R.id.action_mapsFragment_to_geoFragment)
        }
        binding.searchFab.setOnClickListener {
            customAlertDialogView = MaterialDialogLayoutBinding.inflate(layoutInflater, null, false)

            MaterialAlertDialogBuilder(requireActivity())
                .setView(customAlertDialogView.root.rootView)
                .setTitle(resources.getString(R.string.searchByYear))
                .setPositiveButton(
                    resources.getString(R.string.search)
                ) { dialogInterface, i ->
                    filterDate()
                }
                .setNegativeButton(
                    resources.getString(R.string.reset)
                ) { dialogInterface, i ->
                    observeRemoteDatabase()
                    observeDatabase()
                }
                .show()
        }
        geofenceViewModel.getCurrentUser()

        searchLocation()

    }

    @SuppressLint("SuspiciousIndentation")
    fun checkYearRange(s1: String, s2: String, year1: String, year2: String): Boolean {
        val simpleDateFormat = SimpleDateFormat("yyyy")
        val date1: Date? = simpleDateFormat.parse(s1)
        val date2: Date? = simpleDateFormat.parse(s2)
        val date3: Date? = simpleDateFormat.parse(year1)
        val date4: Date? = simpleDateFormat.parse(year2)
        return date3!! >= date1 && date3 <= date2 && date4!! <= date2 && date4 >= date1
    }

    @SuppressLint("MissingPermission", "PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                requireContext(),
                R.raw.mapstyle
            )
        )
        map.isMyLocationEnabled = true
        map.setOnMapLongClickListener(this)
        map.isBuildingsEnabled = false
        map.uiSettings.apply {
            isMyLocationButtonEnabled = true
            isMapToolbarEnabled = true
        }
        adapter = MarkerInfoWindowAdapter(requireActivity())
        map.setInfoWindowAdapter(adapter)
        map.setOnInfoWindowClickListener {
            findNavController().navigate(
                MapsFragmentDirections.actionMapsFragmentToProfile(it.snippet.toString())
            )
            Log.d("Snippet", it.snippet.toString())
        }
        onGeofenceReady()
        observeDatabase()
        observeRemoteDatabase()
    }

    private fun onGeofenceReady() {
        if (geofenceViewModel.geofenceReady) {
            geofenceViewModel.geofenceReady = false
            geofenceViewModel.geofencePrepared = true
            displayInfoMessage()
            zoomToSelectedLocation()
        }
    }

    private fun zoomToSelectedLocation() {
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(geofenceViewModel.geoLatLng, 10f),
            2000,
            null
        )
    }

    private fun displayInfoMessage() {
        lifecycleScope.launch {
            binding.infoMessageTextView.show()
            delay(2000)
            binding.infoMessageTextView.animate().alpha(0f).duration = 800
            delay(1000)
            binding.infoMessageTextView.hide()
        }
    }


    //observeFB
    private fun observeDatabase() {

        geofenceViewModel.readGeofences.observe(viewLifecycleOwner) { geofenceEntity ->
            map.clear()
            geofenceEntity.forEach { geofence ->
                viewLifecycleOwner.lifecycleScope.launch {
                    geofenceViewModel.getUserOfGeofence(geofence.createdBy!!).collectLatest {
                        drawCircle(LatLng(geofence.latitude, geofence.longitude), geofence.radius)
                        drawMarker(
                            LatLng(geofence.latitude, geofence.longitude),
                            geofence.name,
                            geofence.time,
                            geofence.year,
                            it.info?.name!!,
                        )
                    }
                }
            }
        }
    }


    private fun observeRemoteDatabase() {
        geofenceViewModel.listOfAllGeoFences.observe(viewLifecycleOwner) { geo ->
            map.clear()
            geo.info!!.forEach { geofence ->
                viewLifecycleOwner.lifecycleScope.launch {
                    geofenceViewModel.getUserOfGeofence(geofence.createdBy!!).collectLatest {
                        drawCircle(
                            LatLng(geofence.latitude, geofence.longitude),
                            geofence.radius
                        )
                        drawMarker(
                            LatLng(geofence.latitude, geofence.longitude),
                            geofence.name, geofence.time, geofence.year, it.info?.name!!
                        )
                    }

                }
            }
        }

    }

    private fun checkOverlaying(location: LatLng, radius: Double): Boolean {
        var d = sqrt(
            (location.latitude - 53.89665084304891).pow(2.0) + (location.longitude - 27.595388777554035).pow(
                2.0
            )
        )
        return d < radius + 200
    }


    override fun onMapLongClick(location: LatLng) {
        if (hasBackgroundLocationPermission(requireContext())) {
            if (geofenceViewModel.geofencePrepared) {
                setupGeofence(location)
            } else {
                Toast.makeText(
                    requireContext(),
                    "You need to create a geofence",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            requestsBackgroundLocationPermission(this)
        }
    }

    private fun zoomToGeofence(center: LatLng, radius: Double) {
        map.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                geofenceViewModel.getBounds(center, radius), 10
            ), 1000, null
        )
    }

    private fun drawCircle(location: LatLng, radius: Double) {
        circle = map.addCircle(
            CircleOptions().center(location).radius(radius)
                .strokeColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.purple_700
                    )
                )
                .fillColor(ContextCompat.getColor(requireContext(), R.color.purple_200))
        )
    }

    private fun drawMarker(
        location: LatLng,
        name: String,
        time: String,
        year: String,
        createdBy: String
    ) {
        val years = "$time-$year"
        val title = name + "_" + years

        map.addMarker(
            MarkerOptions().position(location).title(title).snippet(createdBy)
        )!!.showInfoWindow()

    }


    private fun setupGeofence(location: LatLng) {
        lifecycleScope.launch {
            if (geofenceViewModel.checkDeviceLocationSettings(requireContext())) {
                binding.geofencesFab.disable()
                binding.addGeofenceFab.disable()
                binding.geofenceProgressBar.show()
                drawCircle(location, geofenceViewModel.geoRadius)
                drawMarker(
                    location,
                    geofenceViewModel.geoName,
                    geofenceViewModel.time,
                    geofenceViewModel.year,
                    geofenceViewModel.userName
                )
                Log.d("GeofenceLoc", geofenceViewModel.geoName)
                delay(2000)
                zoomToGeofence(circle.center, circle.radius)
                delay(2000)
                geofenceViewModel.addGeofenceToDatabase(location, requireActivity())
                observeRemoteDatabase()
                Log.d("GeofenceLoc", "${location.latitude}")

                delay(2000)
                geofenceViewModel.resetSharedValues()

                binding.geofencesFab.enable()
                binding.addGeofenceFab.enable()
                binding.geofenceProgressBar.hide()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please enable Location Settings.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireActivity()).build().show()
        } else {
            requestsBackgroundLocationPermission(this)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        onGeofenceReady()

    }

    private fun filterDate() {
        val yearFirst = customAlertDialogView.yearFirstTextEt.text.toString()
        val yearSecond = customAlertDialogView.yearSecondTextEt.text.toString()
        if (yearFirst.isEmpty() || yearSecond.isEmpty()) {
            Toast.makeText(requireActivity(), "Enter years range", Toast.LENGTH_LONG)
                .show()

        } else {
            geofenceViewModel.listOfAllGeoFences.observe(viewLifecycleOwner) { geo ->
                map.clear()
                geo.info!!.filter { i ->
                    checkYearRange(
                        yearFirst,
                        yearSecond,
                        i.time,
                        i.year
                    )
                }.forEach { geofence ->
                    viewLifecycleOwner.lifecycleScope.launch {
                        geofenceViewModel.getUserOfGeofence(geofence.createdBy!!)
                            .collectLatest {
                                drawCircle(
                                    LatLng(geofence.latitude, geofence.longitude),
                                    geofence.radius
                                )
                                drawMarker(
                                    LatLng(geofence.latitude, geofence.longitude),
                                    geofence.name,
                                    geofence.time,
                                    geofence.year,
                                    it.info?.name!!
                                )
                            }

                    }
                }
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults,
            this
        )
    }

    private fun searchLocation() {
        binding.idSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val location: String = binding.idSearchView.query.toString()
                var addressList: List<Address>? = null
                if (location.isNotEmpty()) {
                    val geocoder = Geocoder(requireActivity())
                    try {
                        addressList = geocoder.getFromLocationName(location, 1)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    val address: Address = addressList!![0]
                    val latLng = LatLng(address.latitude, address.longitude)
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

}