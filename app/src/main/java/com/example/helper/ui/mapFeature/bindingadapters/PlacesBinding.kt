package com.example.helper.ui.mapFeature.bindingadapters

import android.util.Log
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.example.helper.R
import com.example.helper.ui.mapFeature.PlacesViewModel
import com.example.helper.ui.mapFeature.helpers.GeofenceViewModel
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputEditText

@BindingAdapter("setCity")
fun TextView.setCity(prediction: AutocompletePrediction){
    this.text = prediction.getPrimaryText(null).toString()
}

@BindingAdapter("setCountry")
fun TextView.setCountry(prediction: AutocompletePrediction){
    this.text = prediction.getSecondaryText(null).toString()
}

@BindingAdapter("updateSliderValueTextView", "getGeoRadius", requireAll = true)
fun Slider.updateSliderValue(textView: TextView, geofenceViewModel: GeofenceViewModel) {
    updateSliderValueTextView(geofenceViewModel.geoRadius, textView)
    this.addOnChangeListener { _, value, _ ->
        geofenceViewModel.geoRadius = value.toDouble()
        updateSliderValueTextView(geofenceViewModel.geoRadius, textView)
    }
}

fun Slider.updateSliderValueTextView(geoRadius: Double, textView: TextView) {
    val kilometers = geoRadius / 1000
    if (geoRadius >= 1000.0) {
        textView.text = context.getString(R.string.display_kilometers, kilometers.toString())
    } else {
        textView.text = context.getString(R.string.display_meters, geoRadius.toString())
    }
    this.value = geoRadius.toFloat()
}



@BindingAdapter("updateGeofenceName", "enableNextButton", requireAll = true)
fun TextInputEditText.onTextChanged(
    geofenceViewModel: GeofenceViewModel,
    placesViewModel: PlacesViewModel
) {
    this.setText(geofenceViewModel.geoName)
    Log.d("Bindings", geofenceViewModel.geoName)
    this.doOnTextChanged { text, _, _, _ ->
        if (text.isNullOrEmpty()) {
           placesViewModel.enableNextButton(false)
        } else {
           placesViewModel.enableNextButton(true)
        }
        geofenceViewModel.geoName = text.toString()
        Log.d("Bindings", geofenceViewModel.geoName)
    }
}


@BindingAdapter("nextButtonEnabled", "saveGeofenceId", requireAll = true)
fun TextView.placesNextClicked(nextButtonEnabled: Boolean, geofenceViewModel: GeofenceViewModel) {
    this.setOnClickListener {
        if (nextButtonEnabled) {
            geofenceViewModel.id = System.currentTimeMillis().toInt()
            this.findNavController().navigate(R.id.action_placesFragment_to_radiusMapFragment)
        }
    }
}

