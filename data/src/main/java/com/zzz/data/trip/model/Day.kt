package com.zzz.data.trip.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.zzz.data.util.DbUtils

/*
Why add this indices block?? Because whenever there's a change in parent table, especially delete operations, room can easily get all entries with
the corresponding 'id'
otherwise, it has to go through all the entries looking for affected mofos
*/
@Entity(
    tableName = DbUtils.DAY_TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Trip::class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["tripId"])
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
