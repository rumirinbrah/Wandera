package com.zzz.data.common

import android.content.Context
import androidx.core.content.edit
import com.zzz.data.util.PrefUtils

/**
 * Used to store user preferences for app settings
 */
class SettingsPreferences(
    context: Context
) {
    private val prefs = context.getSharedPreferences(PrefUtils.SETTINGS_PREF_NAME , Context.MODE_PRIVATE)

    // --- TICKET HOME ---
    fun setHomeTicketContainer(ticketContainer : Boolean){
        prefs.edit {
            putBoolean(PrefUtils.HOME_TICKET_CONTAINER, ticketContainer)
            apply()
        }
    }
    fun isHomeContainerTicket():Boolean{
        return prefs.getBoolean(PrefUtils.HOME_TICKET_CONTAINER , true)
    }


    // --- CHECKLIST ---
    fun setChecklistTrapeziumBox(isTrapezium : Boolean){
        prefs.edit {
            putBoolean(PrefUtils.CHECKLIST_TRAPEZIUM_CONTAINER,isTrapezium)
            apply()
        }
    }
    fun isChecklistBoxTrapezium():Boolean{
        return prefs.getBoolean(PrefUtils.CHECKLIST_TRAPEZIUM_CONTAINER,true)
    }


}