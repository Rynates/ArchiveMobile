package com.example.helper.ui.profileFeature

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.helper.R
import com.example.helper.databinding.RelativedocLayoutBinding
import com.example.helper.databinding.RelativesLayoutBinding
import com.example.helper.domen.models.RelativeResponce
import java.util.*

class RelativeDocAdapter(private val onItemClick: (RelativeResponce) -> Unit
) :
    RecyclerView.Adapter<RelativeDocAdapter.MyViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RelativeResponce>() {
        override fun areItemsTheSame(
            oldItem: RelativeResponce,
            newItem: RelativeResponce
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: RelativeResponce,
            newItem: RelativeResponce
        ): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun getItemCount(): Int = differ.currentList.size

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): MyViewHolder {

        return MyViewHolder(
            RelativedocLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    fun submitData(listDate: List<RelativeResponce>) {
        differ.submitList(listDate)
    }

    inner class MyViewHolder(
        val binding: RelativedocLayoutBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(relativeResponse: RelativeResponce) {
            relativeResponse.let { relativeItem ->
                with(binding) {
                    Glide
                        .with(binding.root)
                        .load(relativeItem.photo)
                        .centerCrop()
                        .circleCrop()
                        .placeholder(R.drawable.ic_profile)
                        .into(relativeView)

                }
                binding.root.setOnClickListener {
                    onItemClick(relativeItem)
                }
            }
        }
    }
}