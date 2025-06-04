package com.zzz.data.trip.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.zzz.data.util.DbUtils

@Entity(
    tableName = DbUtils.DAY_TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Trip::class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Day(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,
    val dayNo : Int,
    val locationName : String,
    val isDone : Boolean = false,
    val tripId : Long
)
