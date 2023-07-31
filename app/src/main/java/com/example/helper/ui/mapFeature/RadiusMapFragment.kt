package com.example.helper.ui.mapFeature

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.helper.R
import com.example.helper.databinding.FragmentRadiusMapBinding
import com.example.helper.ui.MainActivity
import com.example.helper.ui.mapFeature.helpers.GeofenceViewModel
import com.example.helper.utils.onBackPressedHandler
import com.example.helper.utils.show

class RadiusMapFragment : Fragment() {

    private var _binding: FragmentRadiusMapBinding? = null
    private val binding get() = _binding!!

    private val geofenceViewModel: GeofenceViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRadiusMapBinding.inflate(inflater, container, false)
        binding.sharedViewModel = geofenceViewModel

        onBackPressedHandler<MapsFragment>(this,requireActivity(),viewLifecycleOwner)
        (requireActivity() as MainActivity).binding.bottomNavigationView.show()

        binding.stepBack.setOnClickListener {
            findNavController().navigate(R.id.action_radiusMapFragment_to_placesFragment)
        }

        binding.stepDone.setOnClickListener {
            geofenceViewModel.geoRadius = binding.slider.value.toDouble()
            geofenceViewModel.geofenceReady = true
            findNavController().navigate(R.id.action_radiusMapFragment_to_mapsFragment)
        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}