package com.zzz.core.presentation.permission

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzz.core.presentation.events.UIEvents
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class PermissionViewModel : ViewModel() {

    private val _permissionDialogQueue = mutableStateListOf<String>()
    val permissionDialogQueue : List<String> = _permissionDialogQueue

    private val _events = Channel<UIEvents>()
    val events = _events.receiveAsFlow()

    fun onDismiss(){
        if(_permissionDialogQueue.isNotEmpty())
        {
            _permissionDialogQueue.removeAt(0)
        }
    }

    fun onPermissionResult(
        permission : String,
        granted : Boolean
    ){
        viewModelScope.launch {
            if(!granted){
                _permissionDialogQueue.add(permission)
            }else{
                _events.send(UIEvents.Success)
            }
        }
    }



}