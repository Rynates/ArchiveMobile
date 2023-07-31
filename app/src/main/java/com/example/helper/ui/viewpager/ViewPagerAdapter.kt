package com.example.helper.ui.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.helper.ui.familyFeature.FactsFragment

class ViewPagerAdapter(
    list: ArrayList<FactsFragment>,
    fm:FragmentManager,
    lifecycle: Lifecycle

) : FragmentStateAdapter(fm,lifecycle) {

    private var fragmentList = list
    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment {
      return fragmentList[position]
    }
}