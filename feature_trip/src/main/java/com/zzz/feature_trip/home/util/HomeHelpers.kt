package com.zzz.feature_trip.home.util

import java.util.Date
import kotlin.math.abs
import kotlin.time.Duration.Companion.days

internal fun getDateDifference(startDate : Long, endDate : Long) : Int{
    val start = Date(startDate).date
    val end = Date(endDate).date
    return abs(end - start)
}