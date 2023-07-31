package com.example.helper.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class BaseViewModel<Event : ViewEvent, Action : ViewAction, State : ViewState> :
    ViewModel() {

    private val currentState get() = state.value

    private val initialState: State by lazy { createInitialState() }
    abstract fun createInitialState(): State

    private val _mState = MutableStateFlow(initialState)
    val state = _mState.asStateFlow()


    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    val event = _event.asSharedFlow()

//    fun launchOnUi(block: suspend CoroutineScope.() -> Unit) {
//        viewModelScope.launch { block() }
//    }

    protected fun setUiEvent(newEvent: Event) {
        viewModelScope.launch { _event.emit(newEvent) }
    }

    protected fun setState(reducer: State.() -> State) {
        val newState = currentState.reducer()
        _mState.value = newState
    }
//    fun setEvent(event : Event) {
//        val newEvent = event
//        viewModelScope.launch { _event.emit(newEvent) }
//    }
    abstract fun handleEvent(event: Event)

    //    final override fun dispatchEvent(event: Event) {
//        handleAction(eventToAction(event))
//    }
//
//    abstract fun eventToAction(event: Event): Action

}