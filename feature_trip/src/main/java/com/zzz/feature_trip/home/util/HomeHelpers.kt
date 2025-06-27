package com.zzz.feature_trip.home.util

import com.zzz.core.util.toLocalDateTime
import kotlin.math.abs

internal fun getDateDifference(startDate : Long, endDate : Long) : Int{
    val start = startDate.toLocalDateTime()
    val end = endDate.toLocalDateTime()
    return abs(end.dayOfMonth - start.dayOfMonth)
}