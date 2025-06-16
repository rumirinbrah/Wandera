package com.zzz.feature_trip.overview.data.local

import android.content.Context
import androidx.core.content.edit

/**
 * User preferences for the type of list in Trip Overview. User can switch between pager and a list view
 */
internal class ItineraryLayoutPref (
    private val context: Context
){
    private val prefName = "user_pref"
    private val prefPagerLayout = "pager_layout"

    private val pref = context.getSharedPreferences(prefName,Context.MODE_PRIVATE)

    fun setPagerLayout(pager : Boolean){
        pref.edit {
            putBoolean(prefPagerLayout,pager)
            apply()
        }
    }
    fun isLayoutPager():Boolean{
        return pref.getBoolean(prefPagerLayout,true)
    }


}