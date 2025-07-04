package com.zzz.core.presentation.events

sealed interface UIEvents {

    data class Error(val errorMsg : String) : UIEvents

    data object Success : UIEvents

    data class SuccessWithMsg(val msg : String) : UIEvents

}