package com.example.helper.ui.mapFeature.helpers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.helper.R
import com.example.helper.databinding.GeofencesRowLayoutBinding
import com.example.helper.domen.models.GeoFence
import com.example.helper.ui.mapFeature.PredictionsAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


class GeofencesAdapter(private val geofenceViewModel: GeofenceViewModel) :
    RecyclerView.Adapter<GeofencesAdapter.MyViewHolder>() {

    private var geofenceEntity = mutableListOf<GeoFence>()

    class MyViewHolder(val binding: GeofencesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(geofenceEntity: GeoFence) {
            binding.geofencesEntity = geofenceEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = GeofencesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }


    }
    private fun removeItem(holder: MyViewHolder, position: Int) {
        geofenceViewModel.viewModelScope.launch {
            geofenceViewModel.removeGeofence(geofenceEntity[position])
            geofenceViewModel.removeFromRemote(geofenceEntity[position])
            geofenceEntity[position].createdBy?.let { geofenceViewModel.getUserOfGeofence(it) }
            geofenceViewModel.getLocationOfAllGeofences(holder.binding.root.context)
            showSnackBar(holder, geofenceEntity[position])

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentGeofence = geofenceEntity[position]
        holder.binding.txtOptionDigit.setOnClickListener {
                val popupMenu = PopupMenu(holder.binding.root.context, it)
                popupMenu.inflate(R.menu.menu_geofence)
                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.delete ->
                            removeItem(holder,position)
                    }
                    false
                }
                popupMenu.show()
            }
        holder.bind(currentGeofence)


    }

    private fun showSnackBar(
        holder: MyViewHolder,
        removedItem: GeoFence
    ) {
        Snackbar.make(
            holder.itemView,
            "Removed " + removedItem.name,
            Snackbar.LENGTH_LONG
        ).show()
    }

    override fun getItemCount(): Int {
        return geofenceEntity.size
    }

    fun setData(newGeofenceEntity: MutableList<GeoFence>) {
        val geofenceDiffUtil = PredictionsAdapter.MyDiffUtil(geofenceEntity, newGeofenceEntity)
        val diffUtilResult = DiffUtil.calculateDiff(geofenceDiffUtil)
        geofenceEntity = newGeofenceEntity
        diffUtilResult.dispatchUpdatesTo(this)
    }
}










