package com.example.helper.ui.familyFeature

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.opengl.Visibility
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.helper.R
import com.example.helper.databinding.FragmentFactsBinding
import com.example.helper.domen.models.*
import com.example.helper.ui.MainActivity
import com.example.helper.ui.archiveFeature.ArchivesViewModel
import com.example.helper.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class FactsFragment : BaseFragment(), DatePickerDialog.OnDateSetListener {

    private val viewModel: FamilyViewModel by activityViewModels()
    private val archiveViewModel:ArchivesViewModel by activityViewModels()
    private var _binding: FragmentFactsBinding? = null
    private val args: FactsFragmentArgs by navArgs()
    private val relativeId by lazy { args.relativeId }
    private val linkId by lazy { args.linkId }
    private val binding get() = _binding!!
    private lateinit var createdBy: String
    lateinit var preferences: SharedPreferences
    lateinit var imgUrl: String
    private val calendar = Calendar.getInstance()
    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK
            && result.data != null
        ) {
            val photoUri = result.data!!.data
            imgUrl = photoUri.toString()
            Log.d("Profile", imgUrl)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFactsBinding.inflate(inflater, container, false)
        Log.d("relative", relativeId)
        viewModel.getRelativeByiD(relativeId = relativeId)
        (requireActivity() as MainActivity).binding.bottomNavigationView.show()
        preferences = requireActivity().getSharedPreferences("SHARED", Context.MODE_PRIVATE)

        if (linkId != "relativeId") {
            viewModel.getRelativeByiD(linkId)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.relative.observe(viewLifecycleOwner) {relative->
            with(binding) {
                Glide
                    .with(this@FactsFragment)
                    .load(relative?.photo)
                    .centerCrop()
                    .circleCrop()
                    .placeholder(R.drawable.ic_profile)
                    .into(profileImage)
                relative?.userId?.let { archiveViewModel.filterInventory(it) }

                surname.setText(relative?.surname)
                name.setText(relative?.name)
                secondName.setText(relative?.secondName)
                placeOfLiving.setText(relative?.placeOfLiving)
                birthPlace.setText(relative?.birthPlace)
                livingStatus.setText(relative?.livingStatus)
                birthDate.setText(relative?.birthDate)
                createdBy = relative?.userId!!
                if (relative.relativeDoc.isNullOrEmpty()) {
                    constraintLayout.hide()
                } else {
                    archiveViewModel.docs.observe(viewLifecycleOwner) {
                        it.forEach { document ->
                            constraintLayout.show()
                            likeAnim2.progress = 1f
                            likeAnim2.setOnClickListener {
//                                viewModel.updateRelative(
//                                    relative = Relative(
//                                        id = relative.id,
//                                        relativeDoc = null
//                                    )
//                                )
                                constraintLayout.hide()
                            }
//                                likeAnim2.progress = 0f
//                                constraintLayout.hide()
//
//                            }

                                fundNum.text = document.fundNum.toString()
                                inventoryNum.text = document.number.toString()
                                inventoryFundNum.text = document.inventoryNum.toString()

                            }
                        }
                }
            }
            imgUrl =relative?.photo.toString()
            viewModel.getRelative()
        }
        binding.txtOptionDigit.setOnClickListener {
            val popupMenu = PopupMenu(binding.root.context, it)
            popupMenu.inflate(R.menu.menu_relative)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.delete -> {
                        viewModel.deleteRelative(relativeId)
                        if (linkId != "relativeId") {
                            viewModel.deleteRelative(linkId)
                        }
                        findNavController().navigate(R.id.action_factsFragment_to_familyFragment)
                    }
                    R.id.update -> {
                        editText()
                    }
                    R.id.userProfile -> {
                        findNavController().navigate(
                            FactsFragmentDirections.actionFactsFragmentToProfileFragment(createdBy)
                        )
                        Log.d("Createdby", createdBy)
                    }
                    R.id.relatives -> {
                        findNavController().navigate(
                            FactsFragmentDirections.actionFactsFragmentToRelativeLinksFragment(
                                relativeId = relativeId
                            )
                        )
                        if (linkId != "relativeId") {
                            with(preferences.edit()) {
                                putString("personId", linkId)
                                apply()
                            }
                        }
                        with(preferences.edit()) {
                            putString("personId", relativeId)
                            apply()
                        }
                    }
                }
                false
            }
            popupMenu.show()
        }
        with(binding) {
            save.setOnClickListener {

                val livingStatus = livingStatusEdit.selectedItem
                val livingStatusId = if (livingStatus == resources.getString(R.string.alive)) {
                    resources.getString(R.string.alive)
                } else {
                    resources.getString(R.string.deceased)
                }
                if (linkId != "relativeId") {
                    viewModel.updateRelative(
                        relative = Relative(
                            id = linkId,
                            name = name.text.toString(),
                            surname = surname.text.toString(),
                            secondName = secondName.text.toString(),
                            placeOfLiving = placeOfLiving.text.toString(),
                            birthPlace = birthPlace.text.toString(),
                            livingStatus = livingStatusId,
                            birthDate = birthDate.text.toString(),
                            photo = imgUrl
                        )
                    )
                } else {

                }
                surname.unableFunctions()
                name.unableFunctions()
                secondName.unableFunctions()
                placeOfLiving.unableFunctions()
                birthPlace.unableFunctions()
                birthDate.unableFunctions()
                binding.save.hide()

                mySwitcher.showPrevious()
            }
        }
        binding.birthDate.setOnClickListener {
            val datapicker = DatePickerDialog(
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

    }

    fun editText() {
        with(binding) {
            save.show()
            mySwitcher.showNext()
            surname.enableFunctions()
            name.enableFunctions()
            secondName.enableFunctions()
            placeOfLiving.enableFunctions()
            birthPlace.enableFunctions()
            //livingStatus.enableFunctions()
            if (livingStatus.text.toString() == resources.getString(R.string.alive)) {
                livingStatusEdit.setSelection(0)
            } else {
                livingStatusEdit.setSelection(1)
            }
            profileImage.setOnClickListener {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                launcher.launch(intent)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(year, month, dayOfMonth)
        displayFormattedDate(calendar.timeInMillis)
    }

    private fun displayFormattedDate(timestamp: Long) {
        binding.birthDate.setText(formatter.format(timestamp).toString())
        Log.i("Formatting", timestamp.toString())
    }
}