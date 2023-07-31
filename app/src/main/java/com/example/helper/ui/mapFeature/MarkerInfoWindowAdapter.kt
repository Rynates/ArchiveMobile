package com.example.helper.ui.mapFeature

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.example.helper.R
import com.example.helper.databinding.MarkerInfoContentsBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker


class MarkerInfoWindowAdapter(
    private val context: Context
) : GoogleMap.InfoWindowAdapter {

    override fun getInfoContents(marker: Marker): View {

        val view = MarkerInfoContentsBinding.inflate(LayoutInflater.from(context))
        val str = marker.title
        val str2 = str!!.split("_".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        view.textViewTitle.text = str2[0]
        view.textViewAuthor.text = marker.snippet
        view.textViewTime.text = str2[1]
        view.timeView.text = context.resources.getString(R.string.time)
        view.authorView.text = context.resources.getString(R.string.createdBy)
        view.root.setOnClickListener {
            onItemClickListener?.let {
                it(marker)
            }
        }

        return view.root
    }
    override fun getInfoWindow(marker: Marker): View? {
        return null
    }
    private var onItemClickListener: ((Marker) -> Unit)? = null
    fun setOnItemClickListener(listener:(Marker)->Unit){
        onItemClickListener = listener
    }

}