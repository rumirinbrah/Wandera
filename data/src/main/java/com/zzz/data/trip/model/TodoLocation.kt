package com.zzz.data.trip.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.zzz.data.util.DbUtils

@Entity(
    tableName = DbUtils.TODO_LOCATION_TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Day::class,
            parentColumns = ["id"],
            childColumns = ["dayId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TodoLocation(
    @PrimaryKey(autoGenerate = true)
    val id : Long =0,
    val title : String,
    val isTodo : Boolean = true,
    val dayId : Long
)