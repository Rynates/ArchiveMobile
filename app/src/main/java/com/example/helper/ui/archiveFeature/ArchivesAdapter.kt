package com.example.helper.ui.archiveFeature

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helper.R
import com.example.helper.databinding.ArchivasLayoutBinding
import com.example.helper.domen.models.Archive
import com.example.helper.utils.ItemAnimator
import java.util.*

class ArchivesAdapter(private val archivesViewModel: ArchivesViewModel) :
    RecyclerView.Adapter<ArchivesAdapter.MyViewHolder>() {
    var datasetFiltered: List<Archive> = ArrayList()

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Archive>() {
        override fun areItemsTheSame(oldItem: Archive, newItem: Archive): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Archive, newItem: Archive): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun getItemCount(): Int = differ.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ArchivasLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    fun submitData(listDate: List<Archive>) {
        differ.submitList(listDate)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) =
        holder.bind(differ.currentList[position], position)

    inner class MyViewHolder(val binding: ArchivasLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val lifecycleOwner by lazy {
            binding.root.context as? LifecycleOwner
        }
        fun bind(currentCountry: Archive?, position: Int) {
            currentCountry?.let { archiveItem ->
                binding.name.text = archiveItem.name
                binding.expandableLayout.visibility = if (archiveItem.isExpandable) View.VISIBLE else View.GONE
                if (archiveItem.isExpandable) {
                    binding.arrowImageview.setImageResource(R.drawable.arrow_up)
                } else {
                    binding.arrowImageview.setImageResource(R.drawable.arrow_down)
                }
                val nestedAdaper = NestedAdapter(archivesViewModel)

                binding.childRv.apply {
                    layoutManager = LinearLayoutManager(binding.root.context)
                    setHasFixedSize(true)
                    adapter = nestedAdaper
                    itemAnimator = ItemAnimator()
                    //nestedAdaper.stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY

                }
                nestedAdaper.submitNestedList(archiveItem.fundList!!)
                binding.linearLayout.setOnClickListener {
                    archiveItem.isExpandable = !archiveItem.isExpandable
                    notifyItemChanged(position)
                }

                binding.searchView.setOnQueryTextListener(object :
                    androidx.appcompat.widget.SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        lifecycleOwner?.let {
                            archivesViewModel.archives.observe(
                                it
                            ){
                                it.info?.let { archives -> submitData(archives) }
                            }
                        }
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        nestedAdaper.filterList(newText,archiveItem.fundList!!)
                        return true
                    }

                })
            }
        }
    }
}