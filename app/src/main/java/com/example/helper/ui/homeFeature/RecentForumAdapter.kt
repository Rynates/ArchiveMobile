package com.example.helper.ui.homeFeature

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.helper.databinding.RecentLayoutBinding
import com.example.helper.domen.models.RecentForum

class RecentForumAdapter() :
    RecyclerView.Adapter<RecentForumAdapter.MyViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecentForum>() {
        override fun areItemsTheSame(oldItem: RecentForum, newItem: RecentForum): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RecentForum, newItem: RecentForum): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun getItemCount(): Int = differ.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            RecentLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    fun submitData(listDate: List<RecentForum>) {
        differ.submitList(listDate)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) =
        holder.bind(differ.currentList[position], position)


    inner class MyViewHolder(val binding: RecentLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(currentCountry: RecentForum?, position: Int) {
            currentCountry?.let {
                binding.apply {
                }
            }
        }
    }
}