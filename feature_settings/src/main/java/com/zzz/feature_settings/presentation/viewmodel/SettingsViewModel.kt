package com.zzz.feature_settings.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzz.data.common.SettingsPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    context: Context
) : ViewModel() {

    //settings prefs
    private val settingsPref = SettingsPreferences(context)

    private val _state = MutableStateFlow(AppSettingsState())
    internal val state = _state.asStateFlow()

    init {
        _state.update {
            it.copy(
                ticketLikeHomeContainer = settingsPref.isHomeContainerTicket(),
                checklistTrapeziumBox = settingsPref.isChecklistBoxTrapezium()
            )
        }
    }

    internal fun onAction(action: SettingsAction){
        when(action){
            is SettingsAction.SetChecklistContainer -> {
                setChecklistTrapeziumBox(action.isTrapezium)
            }
            is SettingsAction.SetTicketContainer ->{
                setTicketLikeContainer(action.isTicket)
            }
        }
    }

    private fun setTicketLikeContainer(isTicket : Boolean){
        viewModelScope.launch {
            settingsPref.setHomeTicketContainer(isTicket)
            _state.update {
                it.copy(
                    ticketLikeHomeContainer = isTicket,
                )
            }
        }
    }

    private fun setChecklistTrapeziumBox(isTrapezium : Boolean){
        viewModelScope.launch {
            settingsPref.setChecklistTrapeziumBox(isTrapezium)
            _state.update {
                it.copy(
                    checklistTrapeziumBox = isTrapezium,
                )
            }
        }
    }

}