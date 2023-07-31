package com.example.helper.utils


interface ViewEvent

interface ViewAction

interface ViewState

sealed class StateEvent : ViewEvent {
    object LoadEvents : StateEvent()
    object LoadFunds :StateEvent()
    object AddEvents:StateEvent()
}

sealed class StateAction : ViewAction {
    data class SearchAction(val name: String) : StateAction()
    object AllStaff : StateAction()
}

sealed class DataState<out T>: ViewState {
    object Loading : DataState<Nothing>()
    data class Success<out T>(val data: T) : DataState<T>()
    data class Error<out T>(val callErrors: ErrorEntity) : DataState<T>()

}
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error<out T>(val exception: ErrorEntity) : Result<T>()
    object Loading : Result<Nothing>()
}
//sealed class CallErrors {
//    object ErrorEmptyData : CallErrors()
//    object ErrorServer: CallErrors()
//    data class ErrorException(val throwable: Throwable) : CallErrors()
//}


sealed class ErrorEntity{

    object Network:ErrorEntity()

    object NotFound:ErrorEntity()

    object AccessDenied:ErrorEntity()

    object UnKnown:ErrorEntity()
}


//fun <T> Flow<List<T>>.toResultFlow(): Flow<Result<List<T>>> = this.map {
//    if (it.isNotEmpty()) Result.Success(it)
//    else Result.Error(CallErrors.ErrorEmptyData)
//}
fun <T> Result<T>.reduce(): DataState<T> {
    return when (this) {
        is Result.Success -> DataState.Success(data)
        is Result.Error -> DataState.Error(exception)
        is Result.Loading -> DataState.Loading
    }
}

