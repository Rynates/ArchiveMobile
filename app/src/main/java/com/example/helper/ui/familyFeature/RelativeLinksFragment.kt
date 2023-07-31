package com.example.helper.ui.familyFeature

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.helper.R
import com.example.helper.databinding.FragmentRelativeLinksBinding
import com.example.helper.databinding.RelativeLinkDialogLayoutBinding
import com.example.helper.domen.models.Relative
import com.example.helper.domen.models.RelativeResponce
import com.example.helper.utils.TopSpacingItemDecorations
import com.example.helper.utils.onBackPressedHandler
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RelativeLinksFragment : Fragment() {

    private var _binding: FragmentRelativeLinksBinding? = null
    private val binding get() = _binding!!
    private val args: RelativeLinksFragmentArgs by navArgs()
    private val link by lazy { args.link }
    private val chosedId by lazy { args.chosedId }
    private val viewModel: FamilyViewModel by activityViewModels()
    private lateinit var mAdapter: RelativeLinkAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRelativeLinksBinding.inflate(inflater, container, false)
        onBackPressedHandler<FactsFragment>(this, requireActivity(), viewLifecycleOwner)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preferences: SharedPreferences =
            requireActivity().getSharedPreferences("SHARED", Context.MODE_PRIVATE)
        val personId = preferences.getString("personId", null)

        viewModel.relative.observe(viewLifecycleOwner) {
            val getFather = it?.fatherId?.let { it1 -> viewModel.getLinkRelative(it1) }
            val getMother = it?.motherId?.let { it1 -> viewModel.getLinkRelative(it1) }
            viewLifecycleOwner.lifecycle.coroutineScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        instantCombine(
                            getFather?.transform { father -> emit(father?.copy(link = resources.getString(R.string.father))) },
                            getMother?.transform { mother -> emit(mother?.copy(link = resources.getString(R.string.mother))) }).collect { parents ->
                            submitToAdapter(parents)
                        }
                    }
                }
            }
        }
        mAdapter = RelativeLinkAdapter() {
            findNavController().navigate(
                RelativeLinksFragmentDirections.actionRelativeLinksFragmentToFactsFragment(
                    relativeId = it.id!!
                )
            )
        }
        binding.recyclerViewRelativeLinks.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(TopSpacingItemDecorations(30, 10, 30, 10))
            adapter = mAdapter
        }

        if (link == resources.getString(R.string.father)) {
            viewModel.updateRelative(relative = Relative(id = personId, fatherId = chosedId))
        }
        if (link == resources.getString(R.string.mother)) {
            viewModel.updateRelative(relative = Relative(id = personId, motherId = chosedId))
        }

        val bindingDialog = RelativeLinkDialogLayoutBinding.inflate(layoutInflater, null, false)
        binding.addRelativeLink.setOnClickListener {
            MaterialAlertDialogBuilder(requireActivity())
                .setView(bindingDialog.root.rootView)
                .setTitle(resources.getString(R.string.addRelativeLink))
                .setPositiveButton(
                    resources.getString(
                        R.string.add
                    )
                ) { dialogInterface, i ->
                    val checkLinked =
                        if (bindingDialog.father.isChecked) resources.getString(R.string.father) else if (bindingDialog.mother.isChecked)resources.getString(R.string.mother) else "child"
                    findNavController().navigate(
                        RelativeLinksFragmentDirections.actionRelativeLinksFragmentToFamilyFragment(
                            link = checkLinked
                        )
                    )
                }
                .setNegativeButton(
                    resources.getString(
                        R.string.cancel
                    )
                ) { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
                .show()
        }
    }

    private fun submitToAdapter(list: List<RelativeResponce?>) {
        list.let { mAdapter.submitData(it) }

    }
}

inline fun <reified T> instantCombine(vararg flows: Flow<T>?) = channelFlow {
    val array = Array(flows.size) {
        false to (null as T?) // first element stands for "present"
    }

    flows.forEachIndexed { index, flow ->
        launch {
            if (flow != null) {
                flow.collect { emittedElement ->
                    array[index] = true to emittedElement
                    send(array.filter { it.first }.map { it.second })
                }
            }
        }
    }
}