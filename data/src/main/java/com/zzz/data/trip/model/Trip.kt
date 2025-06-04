package com.zzz.data.trip.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zzz.data.util.DbUtils

@Entity(
    tableName = DbUtils.TRIP_TABLE_NAME
)
data class Trip(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,
    val tripName : String,
    val startDate : Long,
    val endDate : Long
)
