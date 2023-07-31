package com.example.helper.ui.familyFeature

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.datastore.dataStore
import androidx.fragment.app.activityViewModels
import com.example.helper.R
import com.example.helper.databinding.BottomsheetLayoutBinding
import com.example.helper.domen.models.EGender
import com.example.helper.domen.models.LivingStatus
import com.example.helper.domen.models.Relative
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialDialogs
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class CustomSheetDialogFragment : BottomSheetDialogFragment(), OnDateSetListener {

    private var _bottomsheetlayoutbinding: BottomsheetLayoutBinding? = null
    private val viewModel: FamilyViewModel by activityViewModels()
    private val calendar = Calendar.getInstance()
    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val bottomsheetLayoutBinding get() = _bottomsheetlayoutbinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bottomsheetlayoutbinding = BottomsheetLayoutBinding.inflate(inflater, container, false)
        bottomsheetLayoutBinding.editAddCard.setOnClickListener {
            triggerAddRelative()
        }
        bottomsheetLayoutBinding.chooseDate.setOnClickListener {
           var datapicker =  DatePickerDialog(
                requireActivity(),
               R.style.AppTheme_DatePickerDialog,
               this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datapicker.setCanceledOnTouchOutside(true)
            datapicker.show()
        }

        return bottomsheetLayoutBinding.root

    }


    fun triggerAddRelative() {
//        val birthData = bottomsheetLayoutBinding.brthDate.getDate()
        val name = bottomsheetLayoutBinding.editNameCard.text.toString()
        val surname = bottomsheetLayoutBinding.editSurnameCard.text.toString()
        val secondName = bottomsheetLayoutBinding.editSecondName.text.toString()
        val birthPlace = bottomsheetLayoutBinding.editBirthPlaceCard.text.toString()
        val placeOfLiving = bottomsheetLayoutBinding.editPlaceOfLivingCard.text.toString()

        val livingStatus = bottomsheetLayoutBinding.livingStatus.selectedItem
        val livingStatusId =  if(livingStatus == resources.getString(R.string.alive)){
            resources.getString(R.string.alive)
        }else{
            resources.getString(R.string.deceased)
        }

        val myGenderId = if(bottomsheetLayoutBinding.famele.isChecked){
            1
        }else {
            2
        }

        if(name.isEmpty() || surname.isEmpty() || birthPlace.isEmpty()){
            Toast.makeText(requireActivity(),"Please,fill name,surname and birthplace field",Toast.LENGTH_LONG).show()
        }else {

            val relative = Relative(
                livingStatus =  livingStatusId,
                gender = EGender.valueOf(myGenderId),
                birthDate = bottomsheetLayoutBinding.chooseDate.text.toString(),
                birthPlace = birthPlace,
                name = name,
                surname = surname,
                secondName = secondName,
                placeOfLiving = placeOfLiving
            )
            viewModel.addRelative(relative)
            this.dismiss()
        }
    }



    companion object {
        const val TAG = "CustomBottomSheet"
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(year, month, dayOfMonth)

        displayFormattedDate(calendar.timeInMillis)
    }

    private fun displayFormattedDate(timestamp: Long) {
        bottomsheetLayoutBinding.chooseDate.text = formatter.format(timestamp).toString()
        Log.i("Formatting", timestamp.toString())
    }
}