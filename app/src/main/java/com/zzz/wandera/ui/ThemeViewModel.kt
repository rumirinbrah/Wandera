package com.zzz.wandera.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzz.wandera.data.local.ThemePreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ThemeViewModel(
    private val themePreferences: ThemePreferences
) :ViewModel(){

    private val _themeState = MutableStateFlow(ThemeState())
    val themeState = _themeState.asStateFlow()

    init {
        _themeState.update {
            ThemeState(
                isSystemDefault = themePreferences.isSystemDefault(),
                isDarkMode = themePreferences.isDarkMode()
            )
        }
    }

    fun setSystemDefault(systemDefault : Boolean){
        viewModelScope.launch {
            _themeState.update {
                it.copy(
                    isSystemDefault = systemDefault
                )
            }
            themePreferences.setSystemDefault(systemDefault)
        }
    }
    fun setDarkMode(darkMode : Boolean){
        viewModelScope.launch {
            _themeState.update {
                it.copy(isDarkMode = darkMode)
            }
            themePreferences.setDarkMode(darkMode)
        }
    }


}
data class ThemeState(
    val isSystemDefault : Boolean = true,
    val isDarkMode : Boolean = false,
)