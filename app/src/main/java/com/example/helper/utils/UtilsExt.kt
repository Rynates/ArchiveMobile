package com.example.helper.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.room.TypeConverter
import com.example.helper.R
import com.example.helper.ui.archiveFeature.ArchivesFragment
import com.example.helper.ui.familyFeature.FactsFragment
import com.example.helper.ui.familyFeature.FamilyFragment
import com.example.helper.ui.homeFeature.HomeFragment
import com.example.helper.ui.mapFeature.MapsFragment
import com.example.helper.ui.profileFeature.ProfileFragment
import com.google.gson.Gson
import dagger.hilt.android.components.ActivityComponent
import java.text.SimpleDateFormat
import java.util.*

fun Context.toast(msg: CharSequence) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}


inline fun <reified T> onBackPressedHandler(fragment: Fragment, requireActivity:ComponentActivity, lifecycle: LifecycleOwner){
    when(T::class.java){
        HomeFragment::class.java ->  requireActivity.onBackPressedDispatcher.addCallback(lifecycle, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController(fragment).popBackStack(R.id.homeFragment, false)
            }
        })
        MapsFragment::class.java ->  requireActivity.onBackPressedDispatcher.addCallback(lifecycle, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController(fragment).popBackStack(R.id.mapsFragment, false)
            }
        })
        FamilyFragment::class.java -> requireActivity.onBackPressedDispatcher.addCallback(lifecycle,object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController(fragment).popBackStack(R.id.familyFragment, false)
            }
        })
        FactsFragment::class.java -> requireActivity.onBackPressedDispatcher.addCallback(lifecycle,object :OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController(fragment).popBackStack(R.id.factsFragment,false)
            }
        })
        ArchivesFragment::class.java -> requireActivity.onBackPressedDispatcher.addCallback(lifecycle,object :OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController(fragment).popBackStack(R.id.archivesFragment,false)
            }
        })
        ProfileFragment::class.java -> requireActivity.onBackPressedDispatcher.addCallback(lifecycle,object :OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController(fragment).popBackStack(R.id.profileFragment,false)
            }
        })
    }

}
inline fun execute(action: () -> Unit) {
    action()
}

fun hideKeyboard(activity: Activity, view: View) {
    val inputMethodManager =
        activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}


fun View.show(){
    this.visibility = View.VISIBLE
}
fun View.hide(){
    this.visibility = View.GONE
}

fun View.enable(){
    this.isEnabled = true
}

fun View.disable(){
    this.isEnabled = false
}
fun EditText.enableFunctions(){
    this.isClickable = true
    this.isCursorVisible = true
    this.isFocusable = true
}
fun EditText.unableFunctions(){
    this.isClickable = false
    this.isCursorVisible = false
}

fun timeConverter(value: Date):String{
    val sdf = SimpleDateFormat("yyyy")
    val date = sdf.format(value)
    return date
}


fun DatePicker.getDate(): Date {
    val calendar = Calendar.getInstance()
    calendar.set(year, month, dayOfMonth)
    return calendar.time
}


