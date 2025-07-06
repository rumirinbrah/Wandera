package com.zzz.data.note.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.zzz.data.trip.model.Trip
import com.zzz.data.util.DbUtils

@Entity(
    tableName = DbUtils.CHECKLIST_TABLE_NAME ,
    foreignKeys = [
        ForeignKey(
            entity = Trip :: class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["tripId"])
    ]
)
data class ChecklistEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,
    val title : String = "",
    val isChecked : Boolean = false,
    val tripId : Long = 0
)
