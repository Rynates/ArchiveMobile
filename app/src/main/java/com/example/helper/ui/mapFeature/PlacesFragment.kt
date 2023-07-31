package com.example.helper.ui.mapFeature

import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.helper.R
import com.example.helper.databinding.FragmentPlacesBinding
import com.example.helper.ui.MainActivity
import com.example.helper.ui.mapFeature.helpers.GeofenceViewModel
import com.example.helper.utils.hide
import com.example.helper.utils.onBackPressedHandler
import com.example.helper.utils.show
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class  PlacesFragment : Fragment() {

    private var _binding: FragmentPlacesBinding? = null
    private val binding get() = _binding!!

    private val shareViewModel: GeofenceViewModel by activityViewModels()
    private val placeViewModel: PlacesViewModel by viewModels()

    private val mAdapter: PredictionsAdapter by lazy { PredictionsAdapter() }
    private lateinit var placesClient: PlacesClient

    private lateinit var geoCoder: Geocoder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Places.initialize(requireContext(), "AIzaSyCfUMGHFK7RWNwUb206IQm-PkL8sDJ2MJo")
        placesClient = Places.createClient(requireContext())
        geoCoder = Geocoder(requireContext())

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlacesBinding.inflate(inflater, container, false)
        (requireActivity() as MainActivity).binding.bottomNavigationView.show()

        onBackPressedHandler<MapsFragment>(this,requireActivity(),viewLifecycleOwner)

        with(binding) {
            predictionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            predictionsRecyclerView.adapter = mAdapter
        }

        binding.sharedViewModel = shareViewModel
        binding.placesViewModel = placeViewModel
        binding.lifecycleOwner = this

        binding.geofenceLocationEt.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                getPlaces(text)
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(text: Editable?) {
                handleNextButton(text)
            }
        })
        binding.placedNext.setOnClickListener {
            findNavController().navigate(R.id.action_placesFragment_to_radiusMapFragment)
        }

        subscribeToObservers()
        return binding.root
    }



    private fun handleNextButton(text: CharSequence?) {
        if (text.isNullOrEmpty()) {
            placeViewModel.enableNextButton(false)
        }
    }

    private fun subscribeToObservers() {
        lifecycleScope.launch {
            mAdapter.placeId.collectLatest { placeId ->
                if (placeId.isNotEmpty()) {
                    onCitySelected(placeId)
                }
            }
        }
    }


    private fun onCitySelected(placeId: String) {
        val placeFields = listOf(
            Place.Field.ID,
            Place.Field.LAT_LNG,
            Place.Field.NAME
        )
        val request = FetchPlaceRequest.builder(placeId, placeFields).build()
        placesClient.fetchPlace(request)
            .addOnSuccessListener { response ->
                Log.d("PlacesFragment", response.place.name) //well
                shareViewModel.geoLatLng = response.place.latLng!!
                shareViewModel.geoLocationName = response.place.name!!
                shareViewModel.geoCitySelected = true
                binding.geofenceLocationEt.setText(shareViewModel.geoLocationName)
                binding.geofenceLocationEt.setSelection(shareViewModel.geoLocationName.length)
                binding.predictionsRecyclerView.hide()
                placeViewModel.enableNextButton(true)
            }
            .addOnFailureListener { exception ->
                Log.e("PlacesFragment", exception.message.toString())
            }
    }

    private fun getPlaces(text: CharSequence?) {
        if (shareViewModel.checkDeviceLocationSettings(requireContext())) {
            lifecycleScope.launch {
                if (text.isNullOrEmpty()) {
                    mAdapter.setData(emptyList())
                } else {
                    val token = AutocompleteSessionToken.newInstance()

                    val request = FindAutocompletePredictionsRequest.builder()
                        .setCountries(shareViewModel.geoCountryCode)
                        .setSessionToken(token)
                        .setQuery(text.toString())
                        .build()
                    placesClient.findAutocompletePredictions(request)
                        .addOnSuccessListener { response ->
                             Log.d("PlacesFragment", response.autocompletePredictions[0].placeId) //well
                            mAdapter.setData(response.autocompletePredictions)
                            binding.predictionsRecyclerView.scheduleLayoutAnimation()
                        }
                        .addOnFailureListener { exception: Exception? ->
                            if (exception is ApiException) {
                                Log.e("PlacesFragment", exception.statusCode.toString())
                            }
                        }
                }
            }
        } else {
            Toast.makeText(
                requireContext(),
                "Пожалуйста,включите геолокацию.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}