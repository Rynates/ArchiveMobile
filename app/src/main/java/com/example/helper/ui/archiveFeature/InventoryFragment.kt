package com.example.helper.ui.archiveFeature

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnCloseListener
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.helper.R
import com.example.helper.databinding.FragmentInventoryBinding
import com.example.helper.domen.models.Archive
import com.example.helper.domen.models.Document
import com.example.helper.domen.repository.UserRepository
import com.example.helper.ui.MainActivity
import com.example.helper.ui.familyFeature.FamilyFragmentArgs
import com.example.helper.ui.homeFeature.HomeFragment
import com.example.helper.ui.profileFeature.ProfileViewModel
import com.example.helper.utils.TopSpacingItemDecorations
import com.example.helper.utils.onBackPressedHandler
import com.example.helper.utils.show
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*


class InventoryFragment : Fragment() {

    private var _binding: FragmentInventoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ArchivesViewModel by activityViewModels()
    private val archiveViewModel:ProfileViewModel by activityViewModels()
    private val mAdapter: InventoryAdapter by lazy { InventoryAdapter(viewModel,archiveViewModel) }
    private val ags: InventoryFragmentArgs by navArgs()
    private val fundId by lazy { ags.fundId }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInventoryBinding.inflate(inflater, container, false)

        onBackPressedHandler<ArchivesFragment>(this, requireActivity(), viewLifecycleOwner)
        (requireActivity() as MainActivity).binding.bottomNavigationView.show()

        binding.inventoryRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(TopSpacingItemDecorations(30, 10, 30, 10))
            adapter = mAdapter
        }
        viewModel.getInventories(fundId)

        Log.d("FundIdFromNavigation", fundId)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mAdapter.filterList(newText)
                return true
            }
        })

        viewModel.inventoryList.observe(viewLifecycleOwner) {
            it?.let { it1 -> updateInventoryList(it1) }
        }
    }

    private fun updateInventoryList(list: List<Document>) {
        if (view == null) return
        mAdapter.submitNestedList(list)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}