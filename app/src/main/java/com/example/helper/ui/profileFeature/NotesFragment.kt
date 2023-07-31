package com.example.helper.ui.profileFeature

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.helper.R
import com.example.helper.databinding.FragmentNotesBinding
import com.example.helper.domen.models.Document
import com.example.helper.ui.MainActivity
import com.example.helper.ui.archiveFeature.ArchivesViewModel
import com.example.helper.ui.familyFeature.FamilyViewModel
import com.example.helper.utils.BaseFragment
import com.example.helper.utils.TopSpacingItemDecorations
import com.example.helper.utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.launch


@AndroidEntryPoint
class NotesFragment : BaseFragment() {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ArchivesViewModel by activityViewModels()
    private val familyViewModel: FamilyViewModel by activityViewModels()

    var mAdapter: NotesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        (requireActivity() as MainActivity).binding.bottomNavigationView.show()

        mAdapter = NotesAdapter(viewModel, familyViewModel = familyViewModel) {
            findNavController().navigate(
                NotesFragmentDirections.actionNotesFragmentToHome(relativeDocLink = it.id)
            )
        }

        binding.recyclerFunds.apply {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(TopSpacingItemDecorations(30, 10, 30, 10))
            adapter = mAdapter
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val countries = arrayOf(
            resources.getString(R.string.narb),
            resources.getString(R.string.niab),
            resources.getString(R.string.osa)
        )

        val adapter: ArrayAdapter<String> =
            ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, countries)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.archivasEdit.adapter = adapter

        val itemSelectedListener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val item = parent.getItemAtPosition(position) as String
                    viewLifecycleOwner.lifecycle.coroutineScope.launch {
                        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                            launch {
                                viewModel.readInventories.collect { doc ->
                                    viewModel.archives.observe(viewLifecycleOwner) { archive ->
                                        val filterArchive =
                                            archive.info?.filter { it.name == item }
                                        if (!filterArchive.isNullOrEmpty()) {
                                            val filterDoc =
                                                doc.filter { it.archiveId == filterArchive[0].id }
                                            submitAdapter(filterDoc)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        binding.archivasEdit.onItemSelectedListener = itemSelectedListener

    }

    private fun submitAdapter(fundList: List<Document>) {
        mAdapter?.submitNestedList(fundList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}