package com.zzz.feature_trip.create.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal fun Long.toFormattedDate() : String{
    val formatter = SimpleDateFormat("dd MMM", Locale.getDefault())
    return formatter.format(Date(this))
}