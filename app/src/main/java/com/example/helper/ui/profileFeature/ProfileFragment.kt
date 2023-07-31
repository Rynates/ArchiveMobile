package com.example.helper.ui.profileFeature

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavArgs
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.helper.R
import com.example.helper.databinding.FragmentProfileBinding
import com.example.helper.domen.models.Archive
import com.example.helper.domen.models.GeoFence
import com.example.helper.domen.models.User
import com.example.helper.ui.MainActivity
import com.example.helper.ui.familyFeature.FactsFragment
import com.example.helper.ui.homeFeature.HomeFragment
import com.example.helper.ui.mapFeature.MapsFragment
import com.example.helper.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfileFragment : BaseFragment() {


    private val viewModel: ProfileViewModel by viewModels()

    private var _profileFragmentBinding: FragmentProfileBinding? = null
    private val profileFragmentBinding get() = _profileFragmentBinding
    private val args: ProfileFragmentArgs by navArgs()
    private val userName by lazy { args.userName }
    private val createdBy by lazy { args.createdBy }
    private lateinit var email: String
    var isEmailOpen: Boolean = true
    private val mAdapter by lazy { GeoFencesAdapter() }
    private var image: String? = null

    private var imgUrl: String? = ""

    val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK
            && result.data != null
        ) {
            val photoUri = result.data!!.data
            imgUrl = photoUri.toString()
            Log.d("Profile", imgUrl!!)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _profileFragmentBinding = FragmentProfileBinding.inflate(inflater, container, false)
        (requireActivity() as MainActivity).binding.bottomNavigationView.show()

        return profileFragmentBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (userName == "userId" && createdBy == "createdBy") {
            viewModel.getCurrentUser()
            profileFragmentBinding?.emailSw?.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    profileFragmentBinding?.emailButton?.show()
                    viewModel.updateCurrentUser(
                        user = User(
                            isEmailOpen = true
                        )
                    )
                } else {
                    profileFragmentBinding?.emailButton?.hide()
                    viewModel.updateCurrentUser(
                        user = User(
                            isEmailOpen = false
                        )
                    )
                }
            }
            profileFragmentBinding?.recyclerViewGeo?.hide()
            onBackPressedHandler<HomeFragment>(this, requireActivity(), viewLifecycleOwner)

        } else if (createdBy != "createdBy") {
            onBackPressedHandler<HomeFragment>(this, requireActivity(), viewLifecycleOwner).also {
                (requireActivity() as MainActivity).binding.bottomNavigationView.selectedItemId = 0

            }

            viewModel.getUserById(createdBy)
            profileFragmentBinding?.infoUser?.hide()
            profileFragmentBinding?.termFullView?.hide()
            profileFragmentBinding?.emailSw?.hide()
            viewLifecycleOwner.lifecycle.coroutineScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        viewModel.geofences.collect {
                            updateGeofenceList(it)
                        }
                    }
                }
            }
            if (isEmailOpen) {
                profileFragmentBinding?.emailButton?.show()
                profileFragmentBinding?.emailButton?.setOnClickListener {
                    sendEmailFeedback(userEmail = email)
                }
            } else {
                profileFragmentBinding?.emailButton?.hide()
            }
            onBackPressedHandler<FactsFragment>(this, requireActivity(), viewLifecycleOwner)

        } else {
            viewModel.getOtherUserByName(userName)
            Log.d("USERNAME", userName)
            profileFragmentBinding?.infoUser?.hide()
            profileFragmentBinding?.termFullView?.hide()
            profileFragmentBinding?.emailSw?.hide()
            if (isEmailOpen) {
                profileFragmentBinding?.emailButton?.show()
                profileFragmentBinding?.emailButton?.setOnClickListener {
                    sendEmailFeedback(userEmail = email)
                }
            } else {
                profileFragmentBinding?.emailButton?.hide()
            }
            onBackPressedHandler<MapsFragment>(this, requireActivity(), viewLifecycleOwner)

        }

        profileFragmentBinding?.recyclerViewGeo!!.apply {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(TopSpacingItemDecorations(30, 10, 30, 10))
            adapter = mAdapter
        }

        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.user.collect {
                        with(profileFragmentBinding!!) {
                            userUpdateNameText.text = it.name
                            userUpdateNameEdit.setText(it.name)
                            locationTextView.text = it.location
                            locationEdit.setText(it.location)
                            infoTextView.text = it.info
                            infoEdit.setText(it.info)
                            image = it.photo.toString()
                            Glide
                                .with(this@ProfileFragment)
                                .load(image)
                                .centerCrop()
                                .circleCrop()
                                .placeholder(R.drawable.ic_profile)
                                .into(imageView)

                        }
                        email = it.email.toString()
                        profileFragmentBinding?.emailButton?.isVisible = it.isEmailOpen
                        profileFragmentBinding?.emailSw?.isChecked = it.isEmailOpen
                        isEmailOpen = it.isEmailOpen
                        viewModel.getGeoFencesByUserId(it.id!!)
                    }
                }
            }
        }



        profileFragmentBinding?.save?.setOnClickListener {
            val info = profileFragmentBinding?.infoEdit?.text.toString()

            val newName = profileFragmentBinding?.userUpdateNameEdit?.text.toString()
            val location = profileFragmentBinding?.locationEdit?.text.toString()

            Log.d("ProfileFragment", newName)

            if (imgUrl != "") {
                viewModel.updateCurrentUser(
                    user = User(
                        name = newName,
                        photo = imgUrl,
                        location = location,
                        isEmailOpen = isEmailOpen,
                        info = info
                    )
                )
                setChanges()

            } else {
                viewModel.updateCurrentUser(
                    user = User(
                        name = newName,
                        photo = image,
                        location = location,
                        isEmailOpen = isEmailOpen,
                        info = info
                    )
                )
                setChanges()
            }
        }

        profileFragmentBinding?.infoUser?.setOnClickListener { editText() }

        profileFragmentBinding?.noteEdit?.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_notesFragment)
        }

    }

    private fun setChanges() {
        with(profileFragmentBinding!!) {
            mySwitcher.showPrevious()
            infoSwitcher.showPrevious()
            locationSwitcher.showPrevious()
            save.hide()
            termFullView.show()
            infoUser.show()
        }
    }

    fun editText() {
        with(profileFragmentBinding!!) {
            mySwitcher.showNext()
            infoSwitcher.showNext()
            locationSwitcher.showNext()

            termFullView.hide()
            save.show()
            infoUser.hide()
            imageView.setOnClickListener {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                launcher.launch(intent)
            }
        }
    }

    fun sendEmailFeedback(appVersionName: String = "", userEmail: String) {

        val to = userEmail //get from db
        val subject = "User Feedback for Android $appVersionName"
        val body = "My feedback: "

        val uriBuilder = StringBuilder("mailto:" + Uri.encode(to))
        uriBuilder.append("?subject=" + Uri.encode(subject))
        uriBuilder.append("&body=" + Uri.encode(body))
        val uriString = uriBuilder.toString()

        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse(uriString))

        try {
            startActivity(intent)
        } catch (e: Exception) {
            e.localizedMessage?.let { Log.e("LOG_TAG", it) }

            if (e is ActivityNotFoundException) {
                Toast.makeText(
                    context,
                    "No application can handle this request. Please install an email client app.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun updateGeofenceList(list: List<GeoFence>) {
        if (view == null) return
        mAdapter.submitData(list)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _profileFragmentBinding = null
    }

}