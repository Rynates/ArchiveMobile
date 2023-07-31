package com.example.helper.ui.familyFeature

import android.annotation.SuppressLint
import androidx.lifecycle.*
import com.example.helper.domen.models.ApiResponse
import com.example.helper.domen.models.Document
import com.example.helper.domen.models.Relative
import com.example.helper.domen.models.RelativeResponce
import com.example.helper.domen.repository.FamilyRepository
import com.google.android.gms.common.api.Api
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class FamilyViewModel @Inject constructor(
    private val familyRepository: FamilyRepository
) : ViewModel() {

    private var _listOfRelatives = MutableLiveData<List<RelativeResponce>?>()
    val listOfRelatives = _listOfRelatives

    private var _relative = MutableLiveData<RelativeResponce?>()
    val relative = _relative

    init {
        getRelative()
    }

    fun getRelative() {
        viewModelScope.launch {
            val list = familyRepository.getRelatives()
            list?.let {
                _listOfRelatives.value = list.info
            }
        }
    }

    fun getAllRelatives(){
        viewModelScope.launch {
            val list = familyRepository.getAllRelatives()
            list?.let {
                _listOfRelatives.value = list.info
            }
        }
    }


    fun getRelativeByiD(relativeId: String) {
        viewModelScope.launch {
            val relative = familyRepository.getRelativeById(relativeId)
            relative?.let {
                _relative.value = relative.info
            }
        }
    }
    fun getLinkRelative(linkId: String) = flow {
        val link = familyRepository.getRelativeById(linkId)?.info
        emit(link)
    }

    fun updateRelative(relative: Relative) {
        viewModelScope.launch {
            familyRepository.updateRelative(relative)
            val relative = familyRepository.getRelativeById(relativeId = relative.id!!)
            _relative.value = relative?.info

        }
    }

    fun deleteRelative(relativeId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            familyRepository.deleteRelative(relativeId)
            getRelative()
        }
    }

    fun getRelativeOverSearch(
        searchName: String,
        searchSurname: String,
        searchSecondName: String,
        searchPlace: String,
        firstYear: String,
        secondYear: String,
        birthYear: String
    ) {
        viewModelScope.launch {

            val listOfSearch = familyRepository.getAllRelatives()
            if (firstYear.isEmpty() && secondYear.isEmpty()) {
                val filter = listOfSearch.info!!.filter {
                    it.name.equals(searchName)
                            || it.surname.equals(searchSurname)
                            || it.birthPlace.equals(searchPlace)
                            || it.birthDate == birthYear
                }
                _listOfRelatives.value = filter
            } else {
                val filter = listOfSearch.info!!.filter {
                    it.name.equals(searchName)
                            || it.surname.equals(searchSurname)
                            || it.birthPlace.equals(searchPlace)
                            || it.birthDate?.let { it1 ->
                        checkCurrentTimeIsBetweenGivenString(
                            firstYear,
                            secondYear,
                            it1
                        )
                    } == true
                }
                _listOfRelatives.value = filter
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun checkCurrentTimeIsBetweenGivenString(s1: String, s2: String, date: String): Boolean {
        val simpleDateFormat = SimpleDateFormat("yyyy",Locale.getDefault())
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date1: Date? = simpleDateFormat.parse(s1)
        val date2: Date? = simpleDateFormat.parse(s2)
        val date3: Date? = formatter.parse(date)
        val dateFormat: String? = date3?.let { simpleDateFormat.format(it).toString() }
        val date4: Date? = dateFormat?.let { simpleDateFormat.parse(it) }
        return date4!! >= date1 && date4 <= date2
    }

    fun addRelative(relative: Relative) {
        viewModelScope.launch {
            val family = familyRepository.addRelative(relative = relative)
            _listOfRelatives.value = family.info!!
        }
    }
}