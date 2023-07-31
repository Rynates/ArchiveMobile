package com.example.helper.ui.profileFeature

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isNotEmpty
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helper.databinding.NotesLayoutBinding

import com.example.helper.domen.models.Document
import com.example.helper.domen.models.Relative
import com.example.helper.domen.models.RelativeResponce
import com.example.helper.domen.models.User
import com.example.helper.ui.archiveFeature.ArchivesViewModel
import com.example.helper.ui.archiveFeature.NestedAdapter
import com.example.helper.ui.familyFeature.FamilyViewModel
import com.example.helper.utils.CustomLinearLayout
import com.example.helper.utils.ItemAnimator
import com.example.helper.utils.show
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class NotesAdapter(
    private val archivesViewModel: ArchivesViewModel,
    private val familyViewModel: FamilyViewModel,
    private val onItemClick: (Document) -> Unit
) :
    RecyclerView.Adapter<NotesAdapter.NestedViewHolder>() {

    var DIFF_UTIL = object : DiffUtil.ItemCallback<Document>() {
        override fun areItemsTheSame(oldItem: Document, newItem: Document): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Document, newItem: Document): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, DIFF_UTIL)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NestedViewHolder {
        return NestedViewHolder(
            NotesLayoutBinding.inflate(
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

    inner class NestedViewHolder(var binding: NotesLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val lifecycleOwner by lazy {
            binding.root.context as? LifecycleOwner
        }

        fun bind(currentFund: Document?) {
            currentFund?.let { inventory ->
                Log.d("InventoryFund", inventory.fundId)
                binding.inventoryNum.text = inventory.number.toString()
                binding.inventory.text = inventory.name
                binding.fundNum.text = inventory.fundNum.toString()
                binding.inventoryFundNum.text = inventory.inventoryNum.toString()

                binding.likeAnim2.apply {
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
                val nestedAdaper = RelativeDocAdapter() {
                }
                if(binding.documentRecycler.isNotEmpty()){
                    binding.documentRecycler.show()
                }
                binding.documentRecycler.apply {
                    layoutManager = CustomLinearLayout(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
                    setHasFixedSize(true)
                    adapter = nestedAdaper
                    itemAnimator = ItemAnimator()

                }

                binding.inventory.setOnClickListener{
                    onItemClick(inventory)
                }

            }

        }

        private fun addInventoryToDb(inventory: Document) {
            archivesViewModel.viewModelScope.launch(Dispatchers.IO) {
                archivesViewModel.addInventory(inventory.copy(isSaved = true))
            }
        }

        private fun removeInventoryFromDb(inventory: Document) {
            archivesViewModel.viewModelScope.launch {
                archivesViewModel.removeInventory(inventory)
            }
        }
    }

    fun submitNestedList(list: List<Document>) {
        differ.submitList(list)
    }


}