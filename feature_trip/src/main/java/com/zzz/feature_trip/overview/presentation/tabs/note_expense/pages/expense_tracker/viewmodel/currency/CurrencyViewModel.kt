package com.zzz.feature_trip.overview.presentation.tabs.note_expense.pages.expense_tracker.viewmodel.currency

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zzz.feature_trip.overview.domain.UICurrency
import com.zzz.feature_trip.overview.domain.currencySymbolMap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class CurrencyViewModel(

): ViewModel(){

    private val loggingEnabled = true

    private val _state = MutableStateFlow(CurrencyState())
    val state = _state.asStateFlow()


    init {
        log {
            "Init....."
        }
        getCurrencies()
    }

    private fun getCurrencies(){
        viewModelScope.launch {
            log {
                "Getting currencies"
            }
            _state.update {
                it.copy(
                    loading = true
                )
            }
            val currencies = currencySymbolMap.map { (code,symbol)->
                UICurrency(
                    code = code ,
                    symbol = symbol ,
                    emoji = code.toFlagEmoji()
                )
            }
            log {
                "Currencies total ${currencies.size}"
            }
            _state.update {
                it.copy(
                    loading = false,
                    currencies = currencies
                )
            }
        }
    }

    private fun log(msg : ()->String){
        if(loggingEnabled){
            Log.d("currencyVm" , msg())
        }
    }

}
private fun String.toFlagEmoji() : String{
    if(this.length<2){
        return this
    }
    val capsCode = this.take(2).uppercase()
    val emoji = capsCode.map {
        0x1F1E6 + (it.code - 'A'.code)
    }.joinToString(separator = "") { codePoint ->
        String(Character.toChars(codePoint))
    }
    return emoji
}
