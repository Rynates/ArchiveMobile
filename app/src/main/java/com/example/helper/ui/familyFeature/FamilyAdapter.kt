package com.example.helper.ui.familyFeature

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.DiffingChangePayload
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.helper.R
import com.example.helper.databinding.RelativesLayoutBinding
import com.example.helper.domen.models.Fund
import com.example.helper.domen.models.Relative
import com.example.helper.domen.models.RelativeResponce
import com.example.helper.utils.hide
import com.example.helper.utils.show
import java.util.*

private const val ARG_DONE = "arg.done"

class FamilyAdapter(
    private val link:String,
    private val onItemClick: (RelativeResponce) -> Unit
) :
    RecyclerView.Adapter<FamilyAdapter.MyViewHolder>() {

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
            RelativesLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }
    fun filterList(query: String?) {

        if (query != null) {
            val filteredList: MutableList<RelativeResponce> = mutableListOf()
            for (i in differ.currentList) {
                if (i.surname!!.lowercase(Locale.ROOT).contains(query)) {
                    filteredList.add(i)
                }
            }
            if (filteredList.isEmpty()) {
                return
            } else {
               submitData(filteredList)
            }
        }
    }

    fun submitData(listDate: List<RelativeResponce>) {
        differ.submitList(listDate)
    }

    inner class MyViewHolder(
        val binding: RelativesLayoutBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(relativeResponse: RelativeResponce) {
            relativeResponse.let { relativeItem ->
                with(binding) {
                    if(relativeItem.link != "link"){
                        likeAnim2.hide()
                        imageView.show()
                    }
                    else{
                        likeAnim2.show()
                        imageView.hide()
                    }
                    relativeName.text = relativeItem.name
                    relativeSurname.text = relativeItem.surname
                    relativeSecondName.text = relativeItem.secondName
                    Glide
                        .with(binding.root)
                        .load(relativeItem.photo)
                        .centerCrop()
                        .circleCrop()
                        .placeholder(R.drawable.ic_profile)
                        .into(imageView)

                }
                binding.root.setOnClickListener {
                    onItemClick(relativeItem)
                    binding.likeAnim2.progress = 1f

                }
            }
        }
    }
}