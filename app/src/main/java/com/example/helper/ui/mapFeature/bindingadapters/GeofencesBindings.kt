package com.example.helper.ui.mapFeature.bindingadapters

import android.util.Log
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import com.example.helper.ui.mapFeature.PlacesViewModel
import com.example.helper.ui.mapFeature.helpers.GeofenceViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.auto.value.AutoAnnotation


@BindingAdapter("parseCoordinates")
fun TextView.parseCoordinates(value: Double) {
    val coordinate = String.format("%.4f", value)
    this.text = coordinate
}

@BindingAdapter("updateGeofenceTime", requireAll = true)
fun TextInputEditText.onTextChanged(
    sharedViewModel: GeofenceViewModel
) {
    this.setText(sharedViewModel.time)
    Log.d("Bindings", sharedViewModel.time)
    this.doOnTextChanged { text, _, _, _ ->
        sharedViewModel.time = text.toString()
        Log.d("Bindings", sharedViewModel.time)
    }
}
@BindingAdapter("updateGeofenceTime2",requireAll = false)
fun TextInputEditText.onTextChangedTime(
    sharedViewModel: GeofenceViewModel
) {
    this.setText(sharedViewModel.year)
    Log.d("Bindings", sharedViewModel.year)
    this.doOnTextChanged { text, _, _, _ ->
        sharedViewModel.year = text.toString()
        Log.d("Bindings", sharedViewModel.year)
    }
}
