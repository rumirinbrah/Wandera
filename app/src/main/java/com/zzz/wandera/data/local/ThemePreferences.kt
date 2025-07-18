package com.zzz.wandera.data.local

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit

class ThemePreferences(
    private val context: Context
) {

    private val prefName = "user_pref"
    private val prefDarkMode = "dark_mode"
    private val prefSystemDefault = "dark_mode"

    private val pref = context.getSharedPreferences(prefName,MODE_PRIVATE)


    fun isSystemDefault():Boolean{
        return pref.getBoolean(prefSystemDefault,true)
    }
    fun setSystemDefault(systemDefault : Boolean){
        pref.edit {
            putBoolean(prefSystemDefault,systemDefault)
            apply()
        }
    }



    fun isDarkMode():Boolean{
        return pref.getBoolean(prefDarkMode,false)
    }
    fun setDarkMode(darkMode : Boolean){
        pref.edit {
            putBoolean(prefDarkMode,darkMode)
            apply()
        }
    }


}