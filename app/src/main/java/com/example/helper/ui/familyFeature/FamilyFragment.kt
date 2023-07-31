package com.example.helper.ui.familyFeature

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.helper.R
import com.example.helper.databinding.FragmentFamilyBinding
import com.example.helper.domen.models.Relative
import com.example.helper.domen.models.RelativeResponce
import com.example.helper.ui.MainActivity
import com.example.helper.ui.familyFeature.CustomSheetDialogFragment.Companion.TAG
import com.example.helper.ui.homeFeature.HomeFragment
import com.example.helper.ui.profileFeature.ProfileFragment
import com.example.helper.utils.TopSpacingItemDecorations
import com.example.helper.utils.hide
import com.example.helper.utils.onBackPressedHandler
import com.example.helper.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FamilyFragment : Fragment() {

    private val viewModel: FamilyViewModel by activityViewModels()
    lateinit var _binding: FragmentFamilyBinding
    private val binding get() = _binding
    private lateinit var mAdapter: FamilyAdapter
    private val args: FamilyFragmentArgs by navArgs()
    private val linkStatus by lazy { args.link }
    private val docLink by lazy { args.relativeDocLink }
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFamilyBinding.inflate(inflater, container, false)
        handler = Handler()
        binding.swipeRefreshLayout.setOnRefreshListener {
            runnable = Runnable {

                viewModel.getRelative()
                binding.swipeRefreshLayout.isRefreshing = false
            }


            handler.postDelayed(
                runnable,
                3000
            )
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBackPressedHandler<HomeFragment>(this, requireActivity(), viewLifecycleOwner)
        (requireActivity() as MainActivity).binding.bottomNavigationView.show()

        viewModel.listOfRelatives.observe(viewLifecycleOwner) {
            it?.let { it1 -> submitToAdapter(it1) }
        }
        Log.d("DOCUMENT",docLink)
        Log.d("DOCUMENT",linkStatus)

        if (linkStatus != "link") {
            binding.addFamilyFab.hide()
            binding.searchFab.hide()
            viewModel.getAllRelatives()
            binding.searchView.show()

        }
        if (docLink != "relativeDoc") {
            binding.addFamilyFab.hide()
            binding.searchFab.hide()
            binding.searchView.show()
            onBackPressedHandler<ProfileFragment>(this, requireActivity(), viewLifecycleOwner)

        }
        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.listOfRelatives.observe(
                    viewLifecycleOwner
                ) {
                    it?.let { it1 -> submitToAdapter(it1) }
                }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                mAdapter.filterList(newText)
                return true
            }
        })
        Log.d("RELATIVEDOC",docLink)
        mAdapter =  if (linkStatus != "link") {
            FamilyAdapter(linkStatus) {
                findNavController().navigate(
                    FamilyFragmentDirections.actionFamilyFragmentToRelativeLinksFragment(
                        link = linkStatus,
                        chosedId = it.id!!
                    )
                )
            }
        }else {
             FamilyAdapter(linkStatus) {
                findNavController().navigate(
                    FamilyFragmentDirections.actionFamilyFragmentToFactsFragment(relativeId = it.id!!)
                )

            }
        }
        if (docLink != "relativeDoc") {
          mAdapter = FamilyAdapter(linkStatus) {
                viewModel.updateRelative(
                    relative = Relative(
                        id = it.id,
                        livingStatus = it.livingStatus,
                        relativeDoc = docLink
                    )
                )
                findNavController().navigate(
                    FamilyFragmentDirections.actionFamilyFragmentToFactsFragment(relativeId = it.id!!)
                )
            }
        }

        binding.recyclerViewFamily.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(TopSpacingItemDecorations(30, 10, 30, 10))
            adapter = mAdapter
        }
        binding.addFamilyFab.setOnClickListener {
            val modalBottomSheet = CustomSheetDialogFragment()
            modalBottomSheet.show(requireActivity().supportFragmentManager, TAG)
        }
        binding.searchFab.setOnClickListener {
            findNavController().navigate(R.id.action_familyFragment_to_searchFragment)
        }


    }

    fun submitToAdapter(list: List<RelativeResponce>) {
        mAdapter.submitData(list)
    }


}