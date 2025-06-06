package com.zzz.feature_trip.create.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal fun Long.toFormattedDate(format : String = "dd MMM") : String{
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    return formatter.format(Date(this))
}