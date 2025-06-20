package com.zzz.feature_translate.data.local

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit

class TranslatePreferences(
    context: Context
) {

    private val prefName = "user_pref"
    private val prefFirstTime = "first_time"

    private var prefs = context.getSharedPreferences(prefName,MODE_PRIVATE)


    fun setFirstTime(firstTime : Boolean){
        prefs.edit {
            putBoolean(prefFirstTime,firstTime)
            apply()
        }
    }
    fun getFirstTime():Boolean{
        return prefs.getBoolean(prefFirstTime,true)
    }

}