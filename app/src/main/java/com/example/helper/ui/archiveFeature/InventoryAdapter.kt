package com.example.helper.ui.archiveFeature

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.helper.databinding.InventoryLayoutBinding
import com.example.helper.domen.models.Document
import com.example.helper.domen.models.Fund
import com.example.helper.domen.repository.UserRepository
import com.example.helper.ui.profileFeature.ProfileViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class InventoryAdapter(private val archivesViewModel: ArchivesViewModel,private val profileViewModel:ProfileViewModel) :
    RecyclerView.Adapter<InventoryAdapter.NestedViewHolder>() {
    var lifecycleOwner:LifecycleOwner ?=null

    var DIFF_UTIL = object : DiffUtil.ItemCallback<Document>() {
        override fun areItemsTheSame(oldItem: Document, newItem: Document): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem:Document, newItem: Document): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, DIFF_UTIL)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NestedViewHolder {
        return NestedViewHolder(
            InventoryLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: NestedViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    inner class NestedViewHolder(var binding: InventoryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(currentFund: Document?) {
            lifecycleOwner = binding.root.context as? LifecycleOwner

            currentFund?.let { inventory ->
                binding.numberItemTv.text = inventory.number.toString()
                binding.nestedItemTv.text = inventory.name

                binding.likeAnim.apply {
                    progress = if (inventory.isSaved) 1f else 0f
                    setOnClickListener {
                        if (inventory.isSaved) {
                            removeInventoryFromDb(inventory)
                            inventory.isSaved = true
                        } else {
                            try {
                                addInventoryToDb(inventory)
                            } catch (e: IndexOutOfBoundsException) {
                                Log.e("exp", e.message.toString())
                            }
                        }
                        Log.d("NestedAdapter", "clicked position $position")
                        inventory.isSaved = !inventory.isSaved
                        progress = if (inventory.isSaved) 1f else 0f
                    }

                }

            }

        }

        private fun addInventoryToDb(inventory: Document) {
            archivesViewModel.viewModelScope.launch(Dispatchers.IO) {
                val fund = archivesViewModel.getFundById(inventory.fundId)
                profileViewModel.getCurrentUser()
                lifecycleOwner?.lifecycleScope?.launch {
                    profileViewModel.user.collect{user->
                    lifecycleOwner?.lifecycleScope?.launch(Dispatchers.IO) {
                        fund.collect { fund ->
                            fund.info!!.inventoryNum?.let {
                                fund.info.archiveId?.let { it1 ->
                                    inventory.copy(
                                        isSaved = true,
                                        fundNum = fund.info.number,
                                        inventoryNum = it,
                                        relativeList = emptyList(),
                                        userId = user.id,
                                        archiveId = it1
                                    )
                                }
                            }?.let {
                                archivesViewModel.addInventory(
                                    it
                                )
                            }

                        }
                    }
                    }
                }
            }
        }
        private fun removeInventoryFromDb(inventory: Document) {
            archivesViewModel.viewModelScope.launch {
                archivesViewModel.removeInventory(inventory)
            }
        }
    }

    fun filterList(query: String?) {
        if (query != null) {
            val filteredList: MutableList<Document> = mutableListOf()
            for (i in differ.currentList) {
                if (i.name!!.lowercase(Locale.ROOT).contains(query)) {
                    filteredList.add(i)
                }
            }
            if (filteredList.isEmpty()) {
                lifecycleOwner?.let {
                    archivesViewModel.inventoryList.observe(it) {
                        submitNestedList(it)
                    }
                }
            } else {
                submitNestedList(filteredList)
            }
        }
    }

    fun submitNestedList(list: List<Document>) {
        differ.submitList(list)
    }

}