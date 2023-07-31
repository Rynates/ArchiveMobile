package com.example.helper.ui.mapFeature

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.helper.databinding.PredictionsRowLayoutBinding
import com.google.android.libraries.places.api.model.AutocompletePrediction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PredictionsAdapter : RecyclerView.Adapter<PredictionsAdapter.MyViewHolder>(){

    private var predictions = emptyList<AutocompletePrediction>()

    private val _placeId = MutableStateFlow("")
    val placeId: StateFlow<String> get() = _placeId

    class MyViewHolder(val binding: PredictionsRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(prediction: AutocompletePrediction) {
            binding.prediction = prediction
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PredictionsRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentPrediction = predictions[position]
        holder.bind(currentPrediction)

        holder.binding.root.setOnClickListener {
            setPlaceId(predictions[position].placeId)
        }
    }

    override fun getItemCount(): Int {
        return predictions.size
    }

    private fun setPlaceId(placeId: String){
        _placeId.value = placeId
    }

    fun setData(newPredictions: List<AutocompletePrediction>){
        val myDiffUtil = MyDiffUtil(predictions, newPredictions)
        val myDiffUtilResult = DiffUtil.calculateDiff(myDiffUtil)
       // differ.submitList(newPredictions)
        predictions = newPredictions
        myDiffUtilResult.dispatchUpdatesTo(this)
    }

    class MyDiffUtil<T>(
        private val oldList: List<T>,
        private val newList: List<T>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] === newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

//
//    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AutocompletePrediction>() {
//        override fun areItemsTheSame(
//            oldItem: AutocompletePrediction,
//            newItem: AutocompletePrediction
//        ): Boolean {
//            return oldItem.placeId == newItem.placeId
//        }
//
//        override fun areContentsTheSame(
//            oldItem: AutocompletePrediction,
//            newItem: AutocompletePrediction
//        ): Boolean {
//            return oldItem.equals(newItem)
//        }
//
//
//    }
//    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)
}














