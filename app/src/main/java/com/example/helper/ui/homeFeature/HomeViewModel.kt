package com.example.helper.ui.homeFeature

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    //private val forumRepo: ForumRepo,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val lastVisibleItem = MutableStateFlow<Int>(0)

    //val forums:Flow<List<RecentForum>> = forumRepo.getAllForums(lastVisibleItem)

}