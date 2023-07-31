package com.example.helper.ui.archiveFeature

import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.view.*
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helper.R
import com.example.helper.databinding.ArchivasLayoutBinding
import com.example.helper.databinding.FragmentArchivasBinding
import com.example.helper.databinding.MaterialDialogLayoutBinding
import com.example.helper.databinding.NestedArchivItemBinding
import com.example.helper.domen.models.Archive
import com.example.helper.ui.MainActivity
import com.example.helper.ui.homeFeature.HomeFragment
import com.example.helper.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@Suppress("DEPRECATION")
@AndroidEntryPoint
class ArchivesFragment : BaseFragment() {

    private var _binding: FragmentArchivasBinding? = null
    private val binding get() = _binding

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    private val viewModel: ArchivesViewModel by activityViewModels()

    private val mAdapter: ArchivesAdapter by lazy { ArchivesAdapter(viewModel) }
    private var recyclerViewState:Parcelable?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArchivasBinding.inflate(inflater, container, false)

        handler = Handler()
        binding?.swipeRefreshLayout?.setOnRefreshListener {
            runnable = Runnable {

                viewModel.archiveUpdate()
                binding?.swipeRefreshLayout!!.isRefreshing = false
            }

            handler.postDelayed(runnable,
                3000)
        }
        binding?.mainRecyclerView!!.apply {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(TopSpacingItemDecorations(30, 10, 30, 10))
            adapter = mAdapter

        }
        return binding?.root!!
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.archiveUpdate()

        onBackPressedHandler<HomeFragment>(this, requireActivity(), viewLifecycleOwner)

        (requireActivity() as MainActivity).binding.bottomNavigationView.show()

        viewModel.archivesList.observe(viewLifecycleOwner) { archiveId ->
            archiveId?.let {
                updateArchivesList(it)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        recyclerViewState =  binding?.mainRecyclerView?.layoutManager?.onSaveInstanceState()
    }
    private fun updateArchivesList(list: List<Archive>) {
        if (view == null) return
        mAdapter.submitData(list)
    }

    override fun onResume() {
        super.onResume()
        recyclerViewState =  binding?.mainRecyclerView?.layoutManager?.onSaveInstanceState()
        binding?.mainRecyclerView?.layoutManager?.onRestoreInstanceState(recyclerViewState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}