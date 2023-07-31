package com.example.helper.ui.familyFeature

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.helper.R
import com.example.helper.databinding.FragmentSearchBinding
import com.example.helper.ui.MainActivity
import com.example.helper.utils.onBackPressedHandler
import com.example.helper.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _searchFragmentBinding:FragmentSearchBinding?=null
    private val searchFragmentBinding get() = _searchFragmentBinding!!

    private val familyViewModel:FamilyViewModel by  activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         _searchFragmentBinding = FragmentSearchBinding.inflate(inflater, container, false)


        return searchFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBackPressedHandler<FamilyFragment>(this,requireActivity(),viewLifecycleOwner)
        (requireActivity() as MainActivity).binding.bottomNavigationView.show()

        searchFragmentBinding.range.setOnClickListener {
            searchFragmentBinding.searchSwitcher.showNext()
        }

        searchFragmentBinding.editDeleteCard.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_familyFragment)
            familyViewModel.getRelative()
        }
        searchFragmentBinding.editAddCard.setOnClickListener {
            searchRelative()
            findNavController().navigate(R.id.action_searchFragment_to_familyFragment)
        }

    }

    fun searchRelative(){
        val searchName = searchFragmentBinding.editNameCard.text.toString()
        val searchSurname = searchFragmentBinding.editSurnameCard.text.toString()
        val searchSecondName = searchFragmentBinding.editSecondName.text.toString()
        val searchPlace = searchFragmentBinding.editBirthPlaceCard.text.toString()
        val firstYear = searchFragmentBinding.editBirthYearFirst.text.toString()
        val secondYear = searchFragmentBinding.editBirthYearSecond.text.toString()
        val birthYear = searchFragmentBinding.editBirthDate.text.toString()

        familyViewModel.getRelativeOverSearch(searchName,searchSurname,searchSecondName,searchPlace,firstYear,secondYear,birthYear)
    }

}