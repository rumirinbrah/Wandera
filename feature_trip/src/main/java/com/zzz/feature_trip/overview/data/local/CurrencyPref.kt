package com.zzz.feature_trip.overview.data.local

import android.content.Context
import androidx.core.content.edit

class CurrencyPref(
    context : Context
) {
    private val prefName = "user_pref"
    private val defaultCurrency = "currency_default"

    private val pref = context.getSharedPreferences(prefName,Context.MODE_PRIVATE)

    fun setDefaultCurrency(currency : String = "USD"){
        pref.edit {
            putString(defaultCurrency,currency)
            apply()
        }
    }
    fun getDefaultCurrency(): String{
        return pref.getString(defaultCurrency,null) ?: "USD"
    }

}