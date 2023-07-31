package com.example.helper.ui.archiveFeature

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.findFragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.helper.databinding.NestedArchivItemBinding
import com.example.helper.domen.models.Fund
import com.example.helper.utils.show
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class NestedAdapter(private val archivesViewModel: ArchivesViewModel) :
    RecyclerView.Adapter<NestedAdapter.NestedViewHolder>() {

    var DIFF_UTIL = object : DiffUtil.ItemCallback<Fund>() {
        override fun areItemsTheSame(oldItem: Fund, newItem: Fund): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Fund, newItem: Fund): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, DIFF_UTIL)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NestedViewHolder {
        return NestedViewHolder(
            NestedArchivItemBinding.inflate(
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

    inner class NestedViewHolder(var binding: NestedArchivItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(currentFund: Fund?) {
            currentFund?.let { fund ->
                binding.inventoryItemTv.text = fund.inventoryNum
                binding.numberItemTv.text = fund.number.toString()
                binding.nestedItemTv.text = fund.name
                if(fund.digitization != ""){
                    binding.elect.show()
                }

                binding.elect.setOnClickListener {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(fund.digitization))
                    binding.root.context.startActivity(browserIntent)
                }
                binding.root.setOnClickListener {
                        findNavController(it.findFragment()).navigate(
                            ArchivesFragmentDirections.actionArchivesFragmentToInventoryFragment(
                                fund.id
                            )
                        )
                }

            }
        }

    }

    fun submitNestedList(list: List<Fund>) {
        differ.submitList(list)
    }

    fun filterList(query: String?, listFund:List<Fund>) {

        if (query != null) {
            val filteredList: MutableList<Fund> = mutableListOf()
            for (i in listFund) {
                if (i.name!!.lowercase(Locale.ROOT).contains(query)) {
                    filteredList.add(i)
                }
            }
                if (filteredList.isEmpty()) {
                    return
                } else {
                    submitNestedList(filteredList)
                }
        }
    }

    private fun addFundToDb(fund:Fund) {
            archivesViewModel.viewModelScope.launch(Dispatchers.IO) {
                archivesViewModel.addFund(fund.copy(isSaves = true))
            }

    }

    private fun removeFundFromDb(fund:Fund) {
        archivesViewModel.viewModelScope.launch {
            archivesViewModel.removeFund(fund)
        }
    }

}


