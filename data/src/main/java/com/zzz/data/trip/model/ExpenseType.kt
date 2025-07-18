package com.zzz.data.trip.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.zzz.data.util.DbUtils

@Entity(
    tableName = DbUtils.TRACK_EXPENSE_TABLE_NAME ,
    foreignKeys = [
        ForeignKey(
            entity = Trip::class ,
            parentColumns = ["id"] ,
            childColumns = ["tripId"] ,
            onDelete = ForeignKey.CASCADE
        )
    ] ,
    indices = [
        Index(value = ["tripId"])
    ]
)
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0 ,
    val amount: Int = 0 ,
    val title: String? = null ,
    val expenseType: String = "other" ,
    val splitInto: Int? = null ,
    val timestamp : Long = System.currentTimeMillis(),
    val tripId: Long = 0
)
