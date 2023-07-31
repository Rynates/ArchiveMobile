package com.example.helper.ui.profileFeature

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.helper.databinding.GeofencesLayoutBinding
import com.example.helper.domen.models.GeoFence

class GeoFencesAdapter() :
    RecyclerView.Adapter<GeoFencesAdapter.MyViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GeoFence>() {
        override fun areItemsTheSame(
            oldItem: GeoFence,
            newItem:GeoFence
        ): Boolean {
            return oldItem.geoId == newItem.geoId
        }

        override fun areContentsTheSame(
            oldItem: GeoFence,
            newItem: GeoFence
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
            GeofencesLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(differ.currentList[position])

    }

    inner class MyViewHolder(
        val binding: GeofencesLayoutBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(geofence: GeoFence) {
            geofence.let { geofenceItem ->
                with(binding) {
                    nameTextView.text = geofenceItem.name
                    locationValueTextView.text = geofenceItem.location
                    latitudeTextView.text = geofenceItem.latitude.toString()
                    longitudeTextView.text = geofenceItem.longitude.toString()
                }

            }
        }

    }

    fun submitData(listDate: List<GeoFence>) {
        differ.submitList(listDate)
    }

}


