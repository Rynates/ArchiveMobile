package com.example.helper.ui.homeFeature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.helper.R
import com.example.helper.databinding.FragmentHomeBinding
import com.example.helper.domen.models.RecentForum
import com.example.helper.ui.MainActivity
import com.example.helper.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private var _homeFragmentBinding: FragmentHomeBinding? = null
    private val homeFragmentBinding get() = _homeFragmentBinding!!

    var mAdapter: RecentForumAdapter = RecentForumAdapter()
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _homeFragmentBinding = FragmentHomeBinding.inflate(inflater, container, false)
        (requireActivity() as MainActivity).binding.bottomNavigationView.show()

        homeFragmentBinding.archives.setOnClickListener {
            findNavController().navigate(R.id.archives)
        }
        homeFragmentBinding.maps.setOnClickListener {
            if (Permissions.hasLocationPermission(requireActivity())) {
                findNavController().navigate(R.id.maps)
            } else {
                findNavController().navigate(R.id.maps)
            }
        }
        homeFragmentBinding.forum.setOnClickListener {
            findNavController().navigate(R.id.home)
        }
        return homeFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val bolvanka = listOf(
            RecentForum(id = "1", "Nicolas", "Wariors through 1965"),
            RecentForum(id = "2", "Anna", "Wariors throughsfsfs 1965")
        )
        homeFragmentBinding.recyclerViewRecent.apply {
            layoutManager = CustomLinearLayout(activity, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(TopSpacingItemDecorations(30, 10, 30, 10))
            addItemDecoration(DotIndicator())
            adapter = mAdapter
            PagerSnapHelper().attachToRecyclerView(this)

        }

        updateUi(bolvanka)
    }

    private fun updateUi(list: List<RecentForum>) {
        if (view == null) return
        mAdapter.submitData(list)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _homeFragmentBinding = null
    }


}