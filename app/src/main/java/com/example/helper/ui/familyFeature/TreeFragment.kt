package com.example.helper.ui.familyFeature

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.helper.R
import com.example.helper.databinding.FragmentViewPagerBinding
import com.example.helper.ui.viewpager.PagerAccordionTransformer
import com.example.helper.ui.viewpager.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TreeFragment : Fragment() {
    private var _binding: FragmentViewPagerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FamilyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewPagerBinding.inflate(inflater, container, false)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        val adapter = ViewPagerAdapter(
//            fragmentList,
//            requireActivity().supportFragmentManager,
//            lifecycle
//        )
        binding.viewpager.apply {
            this.adapter = adapter
            this.setPageTransformer(PagerAccordionTransformer())
        }
        TabLayoutMediator(
            binding.tabsViewPager, binding.viewpager
        ) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Facts"
                }
                1 -> {
                    tab.text = "Relatives"
                }
            }
        }.attach()
    }
}