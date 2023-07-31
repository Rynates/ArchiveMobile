package com.example.helper.ui.familyFeature

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.helper.R
import com.example.helper.databinding.RelativeLinkLayoutBinding
import com.example.helper.domen.models.RelativeResponce

class RelativeLinkAdapter(
    private val onItemClick: (RelativeResponce) -> Unit
    ) :
    RecyclerView.Adapter<RelativeLinkAdapter.MyViewHolder>() {

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
            RelativeLinkLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }


    fun submitData(listDate: List<RelativeResponce?>) {
        differ.submitList(listDate)
    }

    inner class MyViewHolder(
        val binding: RelativeLinkLayoutBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(relativeResponse: RelativeResponce) {
            relativeResponse.let { relativeItem ->
                with(binding) {
                    infoLinkView.text = relativeItem.link
                    relativeName.text = relativeItem.name
                    relativeSurname.text = relativeItem.surname
                    relativePlace.text = relativeItem.birthPlace
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

//        fun setProgress(progresses: List<Int>) {
//            progresses.forEachIndexed { index, i ->
//                fun setProgress(value: Int) {
//                    binding.setProgress(value)
//                }
//            }
//        }

    }
}