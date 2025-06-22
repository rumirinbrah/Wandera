package com.zzz.core.presentation.permission

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class PermissionViewModel : ViewModel() {

    private val _permissionDialogQueue = mutableStateListOf<String>()
    val permissionDialogQueue : List<String> = _permissionDialogQueue

    fun onDismiss(){
        if(_permissionDialogQueue.isNotEmpty())
        {
            _permissionDialogQueue.removeAt(0)
        }
    }
    fun add(){
        _permissionDialogQueue.add("AA")
    }

    fun onPermissionResult(
        permission : String,
        granted : Boolean
    ){
        if(!granted){
            _permissionDialogQueue.add(permission)
        }
    }



}