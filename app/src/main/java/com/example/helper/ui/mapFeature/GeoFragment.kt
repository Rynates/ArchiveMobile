package com.example.helper.ui.mapFeature

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.helper.R
import com.example.helper.databinding.FragmentGeoBinding
import com.example.helper.ui.mapFeature.helpers.GeofencesAdapter
import com.example.helper.ui.mapFeature.helpers.GeofenceViewModel
import com.example.helper.utils.onBackPressedHandler

class GeoFragment : Fragment() {

    private var _binding: FragmentGeoBinding? = null
    private val binding get() = _binding!!

    private val geofenceViewModel: GeofenceViewModel by activityViewModels()
    private val geofencesAdapter by lazy { GeofencesAdapter(geofenceViewModel) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGeoBinding.inflate(inflater, container, false)
        binding.sharedViewModel = geofenceViewModel

        onBackPressedHandler<MapsFragment>(this,requireActivity(),viewLifecycleOwner)

        //setupToolbar()
        setupRecyclerView()
        observeDatabase()

        return binding.root
    }

//    private fun setupToolbar() {
//        binding.toolbar.setNavigationOnClickListener {
//            findNavController().navigate(R.id.action_geoFragment_to_mapsFragment)
//        }
//    }

    private fun setupRecyclerView() {
        binding.geofencesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.geofencesRecyclerView.adapter = geofencesAdapter
    }
    private fun observeDatabase() {
        geofenceViewModel.readGeofences.observe(viewLifecycleOwner) {
            geofencesAdapter.setData(it)
            binding.geofencesRecyclerView.scheduleLayoutAnimation()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}